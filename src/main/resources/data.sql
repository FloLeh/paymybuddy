INSERT INTO users (username, email, password)
VALUES ('example', 'example@gmail.com', 'foobar'),
       ('example2', 'example2@gmail.com', 'foobar');

INSERT INTO users_connections (user1_id, user2_id) VALUES (1, 2);

INSERT INTO transactions(sender_id, receiver_id, description, amount) VALUES (1, 2, 'remboursement', 12.32);

