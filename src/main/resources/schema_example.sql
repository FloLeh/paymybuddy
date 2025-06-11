CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    account DECIMAL(19,2) NOT NULL DEFAULT 100.00
);

CREATE TABLE users_connections (
    user1_id INT NOT NULL,
    user2_id INT NOT NULL,
    PRIMARY KEY (user1_id, user2_id),
    CONSTRAINT fk_user1 FOREIGN KEY (user1_id) REFERENCES users(id),
    CONSTRAINT fk_user2 FOREIGN KEY (user2_id) REFERENCES users(id)
);

CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT,
    receiver_id INT,
    description VARCHAR(255),
    amount DECIMAL(19,2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_sender FOREIGN KEY (sender_id) REFERENCES users(id),
    CONSTRAINT fk_receiver FOREIGN KEY (receiver_id) REFERENCES users(id)
);
