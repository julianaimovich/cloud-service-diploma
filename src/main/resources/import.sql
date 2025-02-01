INSERT INTO users (login, password) VALUES ('admin', '{noop}adminpasswd');
INSERT INTO users (login, password) VALUES ('user', '{noop}userpasswd');
INSERT INTO authorities (login, authority) VALUES ('admin', 'ROLE_ADMIN');
INSERT INTO authorities (login, authority) VALUES ('user', 'ROLE_USER');