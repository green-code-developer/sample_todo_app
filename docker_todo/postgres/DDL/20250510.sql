CREATE TYPE todo_status AS ENUM ('NEW', 'DOING', 'DONE', 'DELETED');
ALTER TABLE todo ALTER COLUMN todo_status DROP DEFAULT;
update todo set todo_status = 'NEW' where todo_status = '';
ALTER TABLE todo
  ALTER COLUMN todo_status SET DEFAULT 'NEW'::todo_status,
  ALTER COLUMN todo_status TYPE todo_status USING todo_status::todo_status;
