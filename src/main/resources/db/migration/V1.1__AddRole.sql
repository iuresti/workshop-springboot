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

INSERT INTO boards (id, name, user_id) VALUES (1, 'B1', 1);
INSERT INTO boards (id, name, user_id) VALUES (2, 'B2', 1);
INSERT INTO boards (id, name, user_id) VALUES (3, 'B3', 2);
INSERT INTO boards (id, name, user_id) VALUES (4, 'B4', 2);

INSERT INTO columns (id, name, board_id) VALUES (1, 'CO1', 1);
INSERT INTO columns (id, name, board_id) VALUES (2, 'CO2', 1);
INSERT INTO columns (id, name, board_id) VALUES (3, 'CO3', 2);
INSERT INTO columns (id, name, board_id) VALUES (4, 'CO4', 4);

INSERT INTO cards (id, description, column_id) VALUES (1, 'CA1', 1);
INSERT INTO cards (id, description, column_id) VALUES (2, 'CA2', 2);
INSERT INTO cards (id, description, column_id) VALUES (3, 'CA3', 3);
INSERT INTO cards (id, description, column_id) VALUES (4, 'CA4', 4);
