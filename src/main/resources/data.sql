INSERT INTO users (login, password) VALUES ('boss', '{noop}bosspasswd');
INSERT INTO users (login, password) VALUES ('admin', '{noop}adminpasswd');
INSERT INTO authorities (login, authority) VALUES ('boss', 'ROLE_ADMIN');
INSERT INTO authorities (login, authority) VALUES ('admin', 'ROLE_ADMIN');