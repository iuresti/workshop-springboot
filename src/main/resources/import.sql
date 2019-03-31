--users
INSERT INTO users (name, email) VALUES ('Kenneth P. Kerr', 'KennethPKerr@armyspy.com');

INSERT INTO users (name, email) VALUES ('Rebecca J. Owsley', 'RebeccaJOwsley@jourrapide.com');

INSERT INTO users (name, email)VALUES ('Sanford H. Trojan', 'SanfordHTrojan@jourrapide.com');

--Boards
INSERT INTO boards(name, user_id) VALUES ('personal', 1);
INSERT INTO boards(name, user_id) VALUES ('project-1', 1);

INSERT INTO boards(name, user_id) VALUES ('personal', 2);

INSERT INTO boards(name, user_id) VALUES ('project-x', 3);
INSERT INTO boards(name, user_id) VALUES ('project-y', 3);
INSERT INTO boards(name, user_id) VALUES ('project-z', 3);

--Columns
INSERT INTO columns(name, board_id) VALUES ('TO.DO', 1);
INSERT INTO columns(name, board_id) VALUES ('In Progress', 1);
INSERT INTO columns(name, board_id) VALUES ('Done', 1);

INSERT INTO columns(name, board_id) VALUES ('TO.DO', 2);
INSERT INTO columns(name, board_id) VALUES ('In Progress', 2);
INSERT INTO columns(name, board_id) VALUES ('Done', 2);

INSERT INTO columns(name, board_id) VALUES ('TO.DO', 3);
INSERT INTO columns(name, board_id) VALUES ('In Progress', 3);
INSERT INTO columns(name, board_id) VALUES ('Done', 3);
INSERT INTO columns(name, board_id) VALUES ('TO.DO', 4);
INSERT INTO columns(name, board_id) VALUES ('In Progress', 4);
INSERT INTO columns(name, board_id) VALUES ('Done', 4);
INSERT INTO columns(name, board_id) VALUES ('TO.DO', 5);
INSERT INTO columns(name, board_id) VALUES ('In Progress', 5);
INSERT INTO columns(name, board_id) VALUES ('Done', 5);
INSERT INTO columns(name, board_id) VALUES ('TO.DO', 6);
INSERT INTO columns(name, board_id) VALUES ('In Progress', 6);
INSERT INTO columns(name, board_id) VALUES ('Done', 6);

--cards
INSERT INTO cards(description, column_id) VALUES ('Pagar la luz', 1);
INSERT INTO cards(description, column_id) VALUES ('Pagar el agua', 1);
INSERT INTO cards(description, column_id) VALUES ('Ir al banco', 1);
INSERT INTO cards(description, column_id) VALUES ('Ir al mandado', 1);
