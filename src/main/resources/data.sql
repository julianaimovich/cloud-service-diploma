INSERT IGNORE INTO users (login, password) VALUES ('boss', '{noop}bosspasswd');
INSERT IGNORE INTO users (login, password) VALUES ('admin', '{noop}adminpasswd');
INSERT IGNORE INTO authorities (login, authority) VALUES ('boss', 'ROLE_ADMIN');
INSERT IGNORE INTO authorities (login, authority) VALUES ('admin', 'ROLE_ADMIN');