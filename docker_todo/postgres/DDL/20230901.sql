/* 2023/9/1 初回リリース向け */
CREATE SCHEMA todo AUTHORIZATION todo;
ALTER ROLE todo SET search_path = todo;
GRANT ALL ON SCHEMA todo TO todo;
SET search_path TO todo;

/* updated_at(更新日時) のカラムを自動的に設定する */

CREATE OR REPLACE FUNCTION refresh_meta_columns()
RETURNS TRIGGER AS $$
BEGIN
    -- 更新時は常に updated_at を現在時刻にする
    NEW.updated_at = NOW();
    IF (TG_OP = 'INSERT') THEN
        -- 新規作成時は created_at を現在時刻にする
        NEW.created_at = NOW();
    ELSIF (TG_OP = 'UPDATE') THEN
        -- 更新時は OLD の値を維持して上書きを防止する
        NEW.created_at = OLD.created_at;
        NEW.created_by = OLD.created_by;
    END IF;
    RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Spring Session 用テーブル ここから
-- https://github.com/spring-projects/spring-session/blob/main/spring-session-jdbc/src/main/resources/org/springframework/session/jdbc/schema-postgresql.sql
CREATE TABLE SPRING_SESSION (
	PRIMARY_ID CHAR(36) NOT NULL,
	SESSION_ID CHAR(36) NOT NULL,
	CREATION_TIME BIGINT NOT NULL,
	LAST_ACCESS_TIME BIGINT NOT NULL,
	MAX_INACTIVE_INTERVAL INT NOT NULL,
	EXPIRY_TIME BIGINT NOT NULL,
	PRINCIPAL_NAME VARCHAR(100),
	CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);
CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);
CREATE TABLE SPRING_SESSION_ATTRIBUTES (
	SESSION_PRIMARY_ID CHAR(36) NOT NULL,
	ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
	ATTRIBUTE_BYTES BYTEA NOT NULL,
	CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
	CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
);
-- Spring Session 用テーブル ここまで

create table ACCOUNT (
  ACCOUNT_ID bigserial primary key,
  ACCOUNT_STATUS varchar not null default '',
  NAME text not null default '',
  UPDATED_AT timestamp with time zone not null default now(),
  UPDATED_BY bigint not null default -1,
  CREATED_AT timestamp with time zone not null default now(),
  CREATED_BY bigint not null default -1
);
CREATE TRIGGER trigger_account_refresh_meta_columns
    BEFORE INSERT OR UPDATE ON account FOR EACH ROW
        EXECUTE FUNCTION refresh_meta_columns();
alter sequence ACCOUNT_ACCOUNT_ID_SEQ restart with 10001;
/* ACCOUNT_ID -1 は特殊なシステムアカウント(本番でも必要) */
insert into ACCOUNT (ACCOUNT_ID, ACCOUNT_STATUS, NAME) values (-1, 'ERR', 'system');

create table TODO (
  TODO_ID bigserial primary key,
  TODO_STATUS varchar not null default '',
  DETAIL text not null default '',
  DEADLINE timestamp with time zone,
  UPDATED_AT timestamp with time zone not null default now(),
  UPDATED_BY bigint not null default -1,
  CREATED_AT timestamp with time zone not null default now(),
  CREATED_BY bigint not null default -1,
  foreign key (UPDATED_BY) references ACCOUNT(ACCOUNT_ID),
  foreign key (CREATED_BY) references ACCOUNT(ACCOUNT_ID)
);
CREATE TRIGGER trigger_todo_refresh_meta_columns
    BEFORE INSERT OR UPDATE ON todo FOR EACH ROW
        EXECUTE FUNCTION refresh_meta_columns();
alter sequence TODO_TODO_ID_SEQ restart with 10001;
