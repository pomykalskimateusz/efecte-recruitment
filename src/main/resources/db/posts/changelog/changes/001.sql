create table posts
(
    id                      uuid primary key,
    account_id              integer not null,
    content                 varchar(200),
    version                 integer not null,
    created_date_timestamp  timestamp default current_timestamp,
    modified_date_timestamp timestamp
);
