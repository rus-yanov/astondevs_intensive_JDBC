DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS carts;
DROP TABLE IF EXISTS items_in_cart;

CREATE TABLE categories (
	id int auto_increment PRIMARY KEY,
	name varchar(255)
);

CREATE TABLE items (
	id int auto_increment PRIMARY KEY,
	name varchar(255),
	price varchar(255),
	category_id integer,
	FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE carts (
	id int auto_increment PRIMARY KEY,
	description varchar(255)
);

CREATE TABLE items_in_cart (
    id int auto_increment PRIMARY KEY,
	cart_id integer,
	item_id integer,
	FOREIGN KEY (cart_id) REFERENCES carts (id),
	FOREIGN KEY (item_id) REFERENCES track (id),
	UNIQUE (cart_id, item_id)
);

INSERT INTO categories (name) VALUES
       ('Produce'),
       ('Deli'),
       ('Meat'),
       ('Beer&Wine'),
       ('Dairy');

INSERT INTO items (name, price, category_id) VALUES
       ('Mellon', '4', 1),
       ('Carrot', '2', 1),
       ('Strawberry', '6', 1),
       ('Tuna salad', '8', 2),
       ('Sub sandwich', '7', 2),
       ('Pepperoni pizza', '12', 2),
       ('Turkey', '6', 3),
       ('Beef', '15', 3),
       ('Chardonnay', '17', 4),
       ('Whiskey', '25', 4),
       ('Milk', '3', 5),
       ('Eggs', '4', 5);

INSERT INTO carts (description) VALUES
        ('Home'),
        ('For Anton birthday party');

INSERT INTO items_in_cart (cart_id, item_id) VALUES
       (1, 1),
       (1, 2),
       (1, 4),
       (1, 5),
       (1, 7),
       (1, 11),
       (1, 12),
       (2, 3),
       (2, 6),
       (2, 8),
       (2, 9),
       (2, 10);