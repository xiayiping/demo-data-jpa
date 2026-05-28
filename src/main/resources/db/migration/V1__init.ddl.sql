create table todoapp_id_table
(
    id                 varchar(128) not null,
    last_value         bigint,
    batch_size         bigint,
    optimistic_version bigint,
    created_at         timestamp,
    updated_at         timestamp,
    primary key (id)
);

create table todoapp_user
(
    id                 bigint not null,
    username           varchar(32),
    password           varchar(256),
    status             varchar(16),
    optimistic_version bigint,
    primary key (id)
);

create index idx__todoapp_user__username on todoapp_user (username);

create table todoapp_role
(
    id                 bigint not null,
    name               varchar(32),
    status             varchar(16),
    optimistic_version bigint,
    primary key (id)
);

create index idx__todoapp_role__username on todoapp_role (name);

create table todoapp_rel_user_role
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id)
);

create index idx__todoapp_rel_user_role__user_id on todoapp_rel_user_role (user_id, role_id);
create index idx__todoapp_rel_user_role__role_id on todoapp_rel_user_role (role_id, user_id);

insert into todoapp_id_table(id, last_value, batch_size, optimistic_version)
values ('global_locker', 0, 50, 1);


insert into todoapp_user(id, username, password, status, optimistic_version)
values (1, 'xyp', '{noop}123456', 'ACTIVE', 0);

insert into todoapp_role(id, name, status, optimistic_version)
values (1, 'ADMIN', 'ACTIVE', 0);

insert into todoapp_rel_user_role(user_id, role_id)
values (1, 1);
