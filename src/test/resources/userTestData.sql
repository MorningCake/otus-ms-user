TRUNCATE TABLE femida_user.femida_user;

INSERT INTO femida_user.femida_user(id, first_name, last_name, patronymic_name, avatar_url, phone_internal, alpha_ip, token_expired_time, tab_number, email, filter_presets,
                                    bank_id, economic_security_department_id, department_type, is_department_head, can_delegated_to_scc)
VALUES ('98e5046f-7b1c-46b0-bdac-ec22528b3d5d', 'TEST_FIRST_NAME_1', 'TEST_LAST_NAME_1', 'TEST_PATRONYMIC_NAME_1', NULL, 'TEST_PHONE_INTERNAL_1',
        'TEST_ALPHA_IP_1', '1640000000', 'TEST_TAB_NUMBER_1', 'TEST_EMAIL_1', 'TEST_FILTER_PRESET_1', '6a30274d-84cd-4ea1-91a0-91b44499c26d', NULL, 'CENTRALOFFICE', true, true),
       ('9914384a-8fcb-49c9-ba18-efb7616e1707', 'TEST_FIRST_NAME_2', 'TEST_LAST_NAME_2', 'TEST_PATRONYMIC_NAME_2', NULL, 'TEST_PHONE_INTERNAL_2',
        'TEST_ALPHA_IP_2', '1650000000', 'TEST_TAB_NUMBER_2', 'TEST_EMAIL_2', 'TEST_FILTER_PRESET_2', '21b9bea6-5141-4a49-8f78-a00acedcd0bf', NULL, 'TERRITORIALBANK', true, true),
       ('47f70543-ef7f-46d7-a23e-02305d7b1825', 'TEST_FIRST_NAME_3', 'TEST_LAST_NAME_3', 'TEST_PATRONYMIC_NAME_3', NULL, 'TEST_PHONE_INTERNAL_3',
        'TEST_ALPHA_IP_3', '1660000000', 'TEST_TAB_NUMBER_3', 'TEST_EMAIL_3', 'TEST_FILTER_PRESET_3', '21b9bea6-5141-4a49-8f78-a00acedcd0bf', '05d3fdf6-8d52-48bd-9125-8b90ceb2c2c9', 'GOSB', true, true),
       ('6a695f50-58a1-46f4-8d86-fbf7b33b755f', 'TEST_FIRST_NAME_4', 'TEST_LAST_NAME_4', 'TEST_PATRONYMIC_NAME_4', NULL, 'TEST_PHONE_INTERNAL_4',
        'TEST_ALPHA_IP_4', '1600000000', 'TEST_TAB_NUMBER_4', 'TEST_EMAIL_4', 'TEST_FILTER_PRESET_4', '21b9bea6-5141-4a49-8f78-a00acedcd0bf', '05d3fdf6-8d52-48bd-9125-8b90ceb2c2c9', 'GOSB', false, false);