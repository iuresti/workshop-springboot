create table boards
(
    id      bigserial not null,
    name    varchar(100),
    user_id int8      not null,
    primary key (id)
);
create table cards
(
    id          bigserial not null,
    description varchar(2000),
    column_id   int8      not null,
    primary key (id)
);
create table columns
(
    id       bigserial not null,
    name     varchar(30),
    board_id int8      not null,
    primary key (id)
);
create table users
(
    id    bigserial not null,
    email varchar(200),
    name  varchar(300),
    primary key (id)
);
alter table if exists boards
    add constraint UKrkmkpaj3mli699uq55mrdqf4c unique (user_id, name);
alter table if exists columns
    add constraint UKe2c6vij8b7qejtd9l5puwgkaq unique (board_id, name);
alter table if exists users
    add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);
alter table if exists boards
    add constraint FK7kt8hby5livgmjj15f79e9t6v foreign key (user_id) references users;
alter table if exists cards
    add constraint FKlpqsumqub2x95veana5uak4gc foreign key (column_id) references columns;
alter table if exists columns
    add constraint FKiylg7oiwdt1tnoff75rkbihc0 foreign key (board_id) references boards;
