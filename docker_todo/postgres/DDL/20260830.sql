/* 2026/8/30 todo_status から DELETED を削除 */
/* 削除機能が未実装で DELETED を設定する導線がないため、enum の値から除外する */
/* PostgreSQL の enum は値を個別に削除できないため、型を作り直す */

/* 想定外の DELETED が残っている場合は NEW に戻す */
update todo set todo_status = 'NEW' where todo_status = 'DELETED';

alter type todo_status rename to todo_status_old;
create type todo_status as enum ('NEW', 'DOING', 'DONE');
alter table todo alter column todo_status drop default;
alter table todo alter column todo_status type todo_status using todo_status::text::todo_status;
alter table todo alter column todo_status set default 'NEW'::todo_status;
drop type todo_status_old;
