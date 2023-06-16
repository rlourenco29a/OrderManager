CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100)
);

CREATE TABLE items (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE stocks (
    id SERIAL PRIMARY KEY,
    creation_date TIMESTAMP,
    quantity INT,
    item_id BIGINT,
    order_id BIGINT,
    FOREIGN KEY (item_id) REFERENCES items (id)
);

CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    creation_date TIMESTAMP,
    quantity INT,
    complete VARCHAR(100),
    item_id BIGINT,
    user_id BIGINT,
    FOREIGN KEY (item_id) REFERENCES items (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

ALTER TABLE stocks
ADD CONSTRAINT fk_order_id
FOREIGN KEY (order_id) REFERENCES orders(id);

INSERT INTO users (name,email) VALUES ('user2', 'example@email.com');
INSERT INTO users (name,email) VALUES ('user2', 'example@email.com');

INSERT INTO items (name) VALUES ('Banana');
INSERT INTO items (name) VALUES ('Potato');
INSERT INTO items (name) VALUES ('Tomato');
INSERT INTO items (name) VALUES ('Rice');

INSERT INTO stocks (creation_date, quantity, item_id, order_id)
VALUES ('2023-06-16 13:00:00.123456', 10, 1, NULL);
INSERT INTO stocks (creation_date, quantity, item_id, order_id)
VALUES ('2023-06-16 10:30:00.123456', 10, 2, NULL);
INSERT INTO stocks (creation_date, quantity, item_id, order_id)
VALUES ('2023-06-16 11:30:00.123456', 10, 3, NULL);
INSERT INTO stocks (creation_date, quantity, item_id, order_id)
VALUES ('2023-06-16 12:30:00.123456', 10, 4, NULL);