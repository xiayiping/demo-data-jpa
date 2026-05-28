create table todos_person
(
    id                 bigint not null,
    username           varchar(32),
    password           varchar(256),
    status             varchar(16),
    optimistic_version bigint,
    primary key (id)
);

create index idx__todos_person__username on todos_person (username);

create table todos_address
(
    id                 bigint not null,
    person_id          bigint,
    p_id               bigint,
    todos_person_key   bigint,
    street             varchar(32),
    optimistic_version bigint,
    primary key (id)
);

create index idx__todos_address__person_id on todos_address (person_id);

