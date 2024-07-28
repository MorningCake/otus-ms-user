--liquibase formatted sql
--changeset MorningCake:007_proc_addFriendsAndPosts.sql
call otus_user._proc__addRandomFriendsAndPosts();
drop procedure otus_user._proc__addRandomFriendsAndPosts();
drop table otus_user.tmp_post_texts;