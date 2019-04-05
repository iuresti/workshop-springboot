create table roles
(
    name        varchar(100),
    description varchar(500),
    primary key (name)
);

create table roles_by_user
(
    user_id   bigserial REFERENCES users,
    role_name varchar(100) REFERENCES roles,
    PRIMARY KEY (user_id, role_name)
);

INSERT INTO roles (name, description) VALUES ('READ_ONLY', 'Read only');
INSERT INTO roles (name, description) VALUES ('ADMIN', 'Administrator');

INSERT INTO roles_by_user SELECT id, 'ADMIN' FROM users
