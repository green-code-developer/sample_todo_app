/* 2023/9/1 初回リリース向け */

drop schema if exists TODO CASCADE;
create schema if not exists TODO;

/* updated_at(更新日時) のカラムを自動的に設定する */
create or replace function TODO.UPDATED_COLUMN_TRIGGER()
returns trigger as $$
begin
  new.UPDATED_AT = now();
  return new;
end;
$$ language 'plpgsql';

-- Spring Session 用テーブル ここから
-- https://github.com/spring-projects/spring-session/blob/main/spring-session-jdbc/src/main/resources/org/springframework/session/jdbc/schema-postgresql.sql
CREATE TABLE TODO.SPRING_SESSION (
	PRIMARY_ID CHAR(36) NOT NULL,
	SESSION_ID CHAR(36) NOT NULL,
	CREATION_TIME BIGINT NOT NULL,
	LAST_ACCESS_TIME BIGINT NOT NULL,
	MAX_INACTIVE_INTERVAL INT NOT NULL,
	EXPIRY_TIME BIGINT NOT NULL,
	PRINCIPAL_NAME VARCHAR(100),
	CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);
CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON TODO.SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON TODO.SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON TODO.SPRING_SESSION (PRINCIPAL_NAME);
CREATE TABLE TODO.SPRING_SESSION_ATTRIBUTES (
	SESSION_PRIMARY_ID CHAR(36) NOT NULL,
	ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
	ATTRIBUTE_BYTES BYTEA NOT NULL,
	CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
	CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES TODO.SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
);
-- Spring Session 用テーブル ここまで

create table TODO.ACCOUNT (
  ACCOUNT_ID bigserial primary key,
  ACCOUNT_STATUS varchar not null default '',
  NAME text not null default '',
  UPDATED_AT timestamp with time zone not null default now(),
  UPDATED_BY bigint not null default -1,
  CREATED_AT timestamp with time zone not null default now(),
  CREATED_BY bigint not null default -1
);
create trigger UPDATED_ACCOUNT before update or insert
  on TODO.ACCOUNT for each row execute procedure TODO.UPDATED_COLUMN_TRIGGER();
alter sequence TODO.ACCOUNT_ACCOUNT_ID_SEQ restart with 10001;
/* ACCOUNT_ID -1 は特殊なシステムアカウント(本番でも必要) */
insert into ACCOUNT (ACCOUNT_ID, ACCOUNT_STATUS, NAME) values (-1, 'ERR', 'system');

create table TODO.TODO (
  TODO_ID bigserial primary key,
  TODO_STATUS varchar not null default '',
  DETAIL text not null default '',
  DEADLINE timestamp with time zone,
  UPDATED_AT timestamp with time zone not null default now(),
  UPDATED_BY bigint not null default -1,
  CREATED_AT timestamp with time zone not null default now(),
  CREATED_BY bigint not null default -1,
  foreign key (UPDATED_BY) references TODO.ACCOUNT(ACCOUNT_ID),
  foreign key (CREATED_BY) references TODO.ACCOUNT(ACCOUNT_ID)
);
create trigger UPDATE_UPDATED_TODO before update or insert
  on TODO.TODO for each row execute procedure TODO.UPDATED_COLUMN_TRIGGER();
alter sequence TODO.TODO_TODO_ID_SEQ restart with 10001;
