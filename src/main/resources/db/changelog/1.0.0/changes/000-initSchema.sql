CREATE TABLE femida_user.femida_user
(
    id              uuid         NOT NULL,
    first_name      varchar(50)  NOT NULL,
    last_name       varchar(50)  NOT NULL,
    patronymic_name varchar(50),
    tab_number      varchar(100) NOT NULL UNIQUE,
    email           varchar(200) NOT NULL UNIQUE,
    phone_internal  varchar(100),
    alpha_ip        varchar(50),
    avatar_url      varchar(200),
    filter_presets  text,
    CONSTRAINT pk_user
        PRIMARY KEY (id)
);
COMMENT ON TABLE femida_user.femida_user IS 'Пользователи';
COMMENT ON COLUMN femida_user.femida_user.id IS 'Идентификатор';
COMMENT ON COLUMN femida_user.femida_user.first_name IS 'Имя';
COMMENT ON COLUMN femida_user.femida_user.last_name IS 'Фамилия';
COMMENT ON COLUMN femida_user.femida_user.patronymic_name IS 'Отчество';
COMMENT ON COLUMN femida_user.femida_user.tab_number IS 'Корпоративный табельный номер. Уникальный для каждого сотрудника атрибут. Должен соответствовать номеру в кадровой структуре Сбера';
COMMENT ON COLUMN femida_user.femida_user.email IS 'Email (alpha). Он же логин пользователя';
COMMENT ON COLUMN femida_user.femida_user.phone_internal IS 'Внутренний номер телефона';
COMMENT ON COLUMN femida_user.femida_user.alpha_ip IS 'Ip ПК пользователя в домене alfa';
COMMENT ON COLUMN femida_user.femida_user.avatar_url IS 'Ссылка на аватар пользователя';


CREATE TABLE femida_user.business_role
(
    id    uuid         NOT NULL,
    code  varchar(50)  NOT NULL,
    title varchar(100) NOT NULL UNIQUE,
    CONSTRAINT pk_business_role
        PRIMARY KEY (id)
);
COMMENT ON TABLE femida_user.business_role IS 'Бизнес роль системы';
COMMENT ON COLUMN femida_user.business_role.id IS 'Идентификатор';
COMMENT ON COLUMN femida_user.business_role.code IS 'Код';
COMMENT ON COLUMN femida_user.business_role.title IS 'Название';

INSERT INTO femida_user.business_role
VALUES ('b060ff2d-c0e7-43a7-9053-35e11d07122e', 'HEAD', 'Руководитель'),
       ('660d628c-2b6d-4ee1-ac13-ddf8e64a7d5f', 'AUDITOR', 'Аудитор'),
       ('bf677d83-97e7-481d-8372-81091c7189bc', 'EXECUTOR', 'Исполнитель'),
       ('a3b155fe-13fe-4adf-ad37-11fe04e0917d', 'CONTENT_MANAGER', 'Контент-менеджер'),
       ('8f97d864-3e7c-4e77-b8a1-c165afb64c44', 'ADMINISTRATOR', 'Администратор'),
       ('c2b22593-d1e3-4826-ae4c-bef0720437b3', 'DOWNLOADER', 'Оператор выгрузки файлов');

CREATE TABLE femida_user.user_business_role
(
    user_id          uuid NOT NULL,
    business_role_id uuid NOT NULL,
    PRIMARY KEY (user_id, business_role_id)
);

ALTER TABLE femida_user.user_business_role
    ADD CONSTRAINT RelUserToBusinessRole FOREIGN KEY (user_id) REFERENCES femida_user.femida_user (id);
ALTER TABLE femida_user.user_business_role
    ADD CONSTRAINT RelBusinessRoleToUser FOREIGN KEY (business_role_id) REFERENCES femida_user.business_role (id);

CREATE UNIQUE INDEX business_role_code
    ON femida_user.business_role (code);
