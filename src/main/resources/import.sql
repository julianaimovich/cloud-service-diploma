INSERT INTO users (login, password) VALUES ('root', '{noop}rootpasswd');
INSERT INTO users (login, password) VALUES ('admin', '{noop}adminpasswd');
INSERT INTO authorities (login, authority) VALUES ('root', 'ROLE_ADMIN');
INSERT INTO authorities (login, authority) VALUES ('admin', 'ROLE_ADMIN');