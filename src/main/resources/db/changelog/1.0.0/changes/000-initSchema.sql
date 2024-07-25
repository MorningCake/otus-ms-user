create table otus_user.otus_user (
    id          uuid         not null   primary key,
    access_id   uuid,
    biography   text,
    birthdate   date         not null,
    city        text,
    exp         bigint,
    first_name  text         not null,
    "password"  varchar(150) not null,
    second_name text         not null,
    sex         varchar(10)  not null,
    username    text         not null constraint "uk_user_username" unique
);

create table otus_user.roles (
    user_id uuid         not null constraint "FK_user_id" references otus_user.otus_user (id),
    "role"    varchar(30) not null,
    primary key (user_id, "role")
);