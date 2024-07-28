--liquibase formatted sql
--changeset MorningCake:004-addFriendsAndPostsTables
create table otus_user.friends (
    user_id     uuid not null references otus_user.otus_user (id),
    friend_id   uuid not null references otus_user.otus_user (id),
    primary key (user_id, friend_id)
);

create table otus_user.post (
    id       uuid not null primary key,
    "text"   text not null,
    user_id  uuid not null references otus_user.otus_user (id),
    "date"   timestamp with time zone not null
);
--rollback drop table otus_user.friends, otus_user.post
