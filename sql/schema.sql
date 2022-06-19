create table roles
(
  id           serial8,
  service_name varchar(20) not null,
  display_name varchar(30) not null,
  primary key (id),
  unique (service_name),
  unique (display_name)
);

create table users
(
  id         serial8,
  role_id    int8         not null,
  login      varchar(20)  not null,
  password   varchar(200) not null,
  email      varchar(200) not null,
  phone      varchar(20)  not null,
  first_name varchar(30)  not null,
  last_name  varchar(30)  not null,
  primary key (id),
  unique (login),
  foreign key (role_id) references roles (id)
);

create table categories
(
  id   serial8,
  name varchar(30) not null,
  primary key (id),
  unique (name)
);

create table products
(
  id          serial8,
  category_id int8        not null,
  active      bool        not null,
  name        varchar(50) not null,
  description text        not null,
  cost        int4        not null,
  primary key (id),
  unique (name),
  foreign key (category_id) references categories (id)
);

create table reviews
(
  id         serial8,
  user_id    int8 not null,
  product_id int8 not null,
  rating     int2 not null,
  comment    text not null,
  primary key (id),
  foreign key (user_id) references users (id),
  foreign key (product_id) references products (id)
);

create table cart_items
(
  user_id    int8,
  product_id int8,
  amount     int2 not null,
  primary key (user_id, product_id),
  foreign key (user_id) references users (id),
  foreign key (product_id) references products (id)
);

create table statuses
(
  id           serial8,
  service_name varchar(30) not null,
  display_name varchar(50) not null,
  primary key (id),
  unique (service_name),
  unique (display_name)
);

create table orders
(
  id        serial8,
  user_id   int8        not null,
  status_id int8        not null,
  street    varchar(50) not null,
  house     varchar(20) not null,
  apartment varchar(20) not null,
  primary key (id),
  foreign key (user_id) references users (id),
  foreign key (status_id) references statuses (id)
);

create table order_products
(
  order_id   int8,
  product_id int8,
  amount     int2 not null,
  primary key (order_id, product_id),
  foreign key (order_id) references orders (id),
  foreign key (product_id) references products (id)
);
