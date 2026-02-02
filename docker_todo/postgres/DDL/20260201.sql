drop table if exists test_all;
CREATE TABLE test_all
(
    col_smallint         smallint,
    col_smallserial      smallserial,
    col_integer          integer,
    col_serial           serial,
    col_bigint           bigint,
    col_bigserial        bigserial primary key,
    col_real             real,
    col_double_precision double precision,
    col_numeric          numeric(10, 2),
    col_boolean          boolean,
    col_char             character(10),
    col_varchar          character varying(50),
    col_text             text,
    col_date             date,
    col_time             time without time zone,
    col_time_tz          time with time zone,
    col_timestamp        timestamp without time zone,
    col_timestamp_tz     timestamp with time zone,
    col_interval interval,
    col_bytea            bytea,
    col_uuid             uuid,
    col_json             json,
    col_jsonb            jsonb,
    col_xml              xml,
    col_inet             inet,
    col_cidr             cidr,
    col_macaddr          macaddr,
    col_box              box,
    col_point            point,
    col_line             line,
    col_lseg             lseg,
    col_path             path,
    col_polygon          polygon,
    col_circle           circle,
    col_todo_status      todo_status
);

drop table if exists test_no_pk;
CREATE TABLE test_no_pk
(
    col_text text
);

drop table if exists test_one_pk;
CREATE TABLE test_one_pk
(
    col_bigserial bigserial primary key,
    col_text      text
);

drop table if exists test_two_pk;
CREATE TABLE test_two_pk
(
    col_integer_pk1 BIGSERIAL,
    col_integer_pk2 BIGSERIAL,
    col_text        TEXT,
    PRIMARY KEY (col_integer_pk1, col_integer_pk2)
);

drop table if exists test_all_with_default;
CREATE TABLE test_all_with_default
(
    col_smallint         smallint                 NOT NULL DEFAULT 0,
    col_smallserial      smallserial              NOT NULL,
    col_integer          integer                  NOT NULL DEFAULT 0,
    col_serial           serial                   NOT NULL,
    col_bigint           bigint                   NOT NULL DEFAULT 0,
    col_bigserial        bigserial                NOT NULL PRIMARY KEY,
    col_real             real                     NOT NULL DEFAULT 0.0,
    col_double_precision double precision         NOT NULL DEFAULT 0.0,
    col_numeric          numeric(10, 2)           NOT NULL DEFAULT 0.0,
    col_boolean          boolean                  NOT NULL DEFAULT false,
    col_char             character(10)            NOT NULL DEFAULT '',
    col_varchar          character varying(50)    NOT NULL DEFAULT '',
    col_text             text                     NOT NULL DEFAULT '',
    col_date             date                     NOT NULL DEFAULT CURRENT_DATE,
    col_time             time without time zone NOT NULL DEFAULT CURRENT_TIME,
    col_time_tz          time with time zone      NOT NULL DEFAULT CURRENT_TIME,
    col_timestamp        timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    col_timestamp_tz     timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    col_interval interval NOT NULL DEFAULT INTERVAL '0',
    col_bytea            bytea                    NOT NULL DEFAULT '\x'::bytea,
    col_uuid             uuid                     NOT NULL DEFAULT gen_random_uuid(),
    col_json             json                     NOT NULL DEFAULT '{}'::json,
    col_jsonb            jsonb                    NOT NULL DEFAULT '{}'::jsonb,
    col_xml              xml                      NOT NULL DEFAULT '<root/>'::xml,
    col_inet             inet                     NOT NULL DEFAULT '0.0.0.0',
    col_cidr             cidr                     NOT NULL DEFAULT '0.0.0.0/32',
    col_macaddr          macaddr                  NOT NULL DEFAULT '00:00:00:00:00:00',
    col_box              box                      NOT NULL DEFAULT '((0,0),(0,0))'::box,
    col_point            point                    NOT NULL DEFAULT '(0,0)'::point,
    col_line             line                     NOT NULL DEFAULT '{0,1,0}'::line,
    col_lseg             lseg                     NOT NULL DEFAULT '((0,0),(0,0))'::lseg,
    col_path             path                     NOT NULL DEFAULT '((0,0),(0,0))'::path,
    col_polygon          polygon                  NOT NULL DEFAULT '((0,0),(0,0),(0,0))'::polygon,
    col_circle           circle                   NOT NULL DEFAULT '((0,0),0)'::circle,
    col_todo_status      todo_status              NOT NULL DEFAULT 'NEW'
);
