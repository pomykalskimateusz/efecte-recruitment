create table "POST"
(
    "ID"                      uuid primary key,
    "ACCOUNT_ID"              bigint not null,
    "CONTENT"                 varchar(200),
    "VERSION"                 integer not null,
    "CREATED_DATE_TIMESTAMP"  timestamp default current_timestamp,
    "MODIFIED_DATE_TIMESTAMP" timestamp
);
