--liquibase formatted sql
--changeset MorningCake:003-addNamesUserSearchIndex
create index idx_users_firstAndLastName on otus_user.otus_user
    (second_name text_pattern_ops, first_name text_pattern_ops);
--rollback drop index otus_user.idx_users_firstAndLastName
