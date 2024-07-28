create or replace procedure otus_user._proc__addRandomFriendsAndPosts()
    language plpgsql as $$
declare
    r record;
    current_post text;
    current_friend_id uuid;
    i int4 = 0;
    j int4 = 0;
    rand int4;
    self_id uuid = 'ac172001-9327-19ff-8193-2770af5f0000';
begin
    for r in (
        select * from otus_user.otus_user
        order by id limit 1000
    ) loop
        begin
            -- добавить 100 друзей для нашего юзера
            if i < 100 then
                insert into otus_user.friends (user_id, friend_id)
                values (self_id, r.id), (r.id, self_id);
            end if;
            raise notice  'Friend :% has been added', i;

            -- добавить пары друзей
            if i%2 = 0 then
                current_friend_id = r.id;
            else
                insert into otus_user.friends (user_id, friend_id)
                values (current_friend_id, r.id), (r.id, current_friend_id);
            end if;
            raise notice  'Friend pair between users :% and :% has been added', current_friend_id, r.id;

            -- добавить по 10 случайных постов каждому новому юзеру
            for j in 1..10 loop
                rand = random()*1250;
                select post into current_post from otus_user.tmp_post_texts where id = rand;

                insert into otus_user.post (id, "text", user_id, "date")
                values ("otus_user".uuid_generate_v4(), current_post, r.id, now());
            end loop;
            i = i + 1;

            raise notice  'Posts for user :% has been added', r.id;
            raise notice  'Success! :%', r.id;
        exception when others then
            raise notice  'Failed! :%', r.id;
            RAISE INFO 'Error Name:%',SQLERRM;
            RAISE INFO 'Error State:%', SQLSTATE;
        end;
    end loop;
end;
$$;