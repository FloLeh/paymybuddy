DELETE FROM users_connections;
DELETE FROM transactions;
DELETE FROM users;

INSERT INTO users (id, email, password, username, account) VALUES (1, 'pierre@example.com', '$2a$12$ahwccH1rN0M4OkSorxzZxunm.hpPC5fdYRAej6Nds19YZd26q.oOy', 'Pierre', 121.3);
INSERT INTO users (id, email, password, username, account) VALUES (2, 'paul@example.com', '$2a$12$ahwccH1rN0M4OkSorxzZxunm.hpPC5fdYRAej6Nds19YZd26q.oOy', 'Paul', 100);
INSERT INTO users (id, email, password, username, account) VALUES (3, 'jacques@example.com', '$2a$12$ahwccH1rN0M4OkSorxzZxunm.hpPC5fdYRAej6Nds19YZd26q.oOy', 'Jacques', 100.01);
INSERT INTO users (id, email, password, username, account) VALUES (4, 'florian@example.com', '$2a$12$ahwccH1rN0M4OkSorxzZxunm.hpPC5fdYRAej6Nds19YZd26q.oOy', 'Florian', 100);
INSERT INTO users (id, email, password, username, account) VALUES (5, 'jean@example.com', '$2a$12$ahwccH1rN0M4OkSorxzZxunm.hpPC5fdYRAej6Nds19YZd26q.oOy', 'Jean', 100);
INSERT INTO users (id, email, password, username, account) VALUES (6, 'luc@example.com', '$2a$12$ahwccH1rN0M4OkSorxzZxunm.hpPC5fdYRAej6Nds19YZd26q.oOy', 'Luc', 100);

INSERT INTO transactions (amount, description, receiver_id, sender_id) VALUES (25, 'Cinema', 1, 4);
INSERT INTO transactions (amount, description, receiver_id, sender_id) VALUES (45.32, 'Restaurant', 4, 2);
INSERT INTO transactions (amount, description, receiver_id, sender_id) VALUES (60, 'Encore des dépenses', 3, 4);
INSERT INTO transactions (amount, description, receiver_id, sender_id) VALUES (18.15, 'Remboursement', 3, 4);
INSERT INTO transactions (amount, description, receiver_id, sender_id) VALUES (21.3, 'Apero', 1, 4);
INSERT INTO transactions (amount, description, receiver_id, sender_id) VALUES (0.15, 'Chewing gum', 1, 4);

INSERT INTO users_connections (user1_id, user2_id) VALUES (4, 2);
INSERT INTO users_connections (user1_id, user2_id) VALUES (4, 1);
INSERT INTO users_connections (user1_id, user2_id) VALUES (4, 3);