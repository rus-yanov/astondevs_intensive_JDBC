DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS carts;
DROP TABLE IF EXISTS items_in_cart;

CREATE TABLE categories (
	id serial PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	name varchar(255)
);

CREATE TABLE items (
	id serial PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	name varchar(255),
	price varchar(255),
	category_id integer,
	FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE carts (
	id serial PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	description varchar(255)
);

CREATE TABLE items_in_cart (
    id serial PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	cart_id integer,
	item_id integer,
	FOREIGN KEY (cart_id) REFERENCES carts (id),
	FOREIGN KEY (item_id) REFERENCES items (id),
	UNIQUE (cart_id, item_id)
);