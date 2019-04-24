create table roles
(
    id          bigserial not null,
    name        varchar(100),
    description varchar(500),
    primary key (id)
);

create table roles_by_user
(
    id        bigserial not null,
    user_id   int8 not null,
    role_id   int8 not null,
    primary key (id)
);

alter table if exists roles
    add constraint FMlpqsumqub2x95veana5uak4gd unique (name);
alter table if exists roles_by_user
    add constraint FNlpqsumqub2x95veana5uak4ge unique (user_id, role_id);
alter table if exists roles_by_user
    add constraint FL7kt8hby5livgmjj15f79e9t6a foreign key (user_id) references users;
alter table if exists roles_by_user
    add constraint FL7kt8hby5livgmjj15f79e9t6b foreign key (role_id) references roles;

INSERT INTO roles (id, name, description) VALUES (1, 'ROLE_ADMIN', 'Administrator');
INSERT INTO roles (id, name, description) VALUES (2, 'ROLE_USER', 'User');

INSERT INTO roles_by_user (id, user_id, role_id) VALUES (1, 1, 2);
INSERT INTO roles_by_user (id, user_id, role_id) VALUES (2, 2, 1);
