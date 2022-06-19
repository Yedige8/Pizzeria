insert into roles(service_name, display_name)
values ('admin', 'Администратор'),
       ('user', 'Пользователь');

insert into users(role_id, login, password, email, phone, first_name, last_name)
values (1, 'admin', 'JEFETUlOMTIzYWRtaW4=', 'admin@admin.kz', '+77777777777', 'Admin', 'Admin'), -- $ADMIN123admin
       (2, 'test_user', 'JFRFU1QxMjN1c2Vy', 'test_user@test.kz', '+77024148245', 'Тест', 'Тестов'); -- $TEST123user

insert into categories(name)
values ('Пиццы'),
       ('Закуски'),
       ('Напитки');

insert into products(category_id, active, name, description, cost)
values (1, true, 'Пепперони', 'Классические пицца пепперони с самыми вкусными колбасками', 2400),
       (1, true, 'Четыре сыра', 'Самые лучшие сорта сыра собранные в одной пицце', 2700);

insert into reviews(user_id, product_id, rating, comment)
values (2, 1, 4, 'Мне понравилась эта пицца, довольно вкусная для своей цены');

insert into statuses(service_name, display_name)
values ('awaiting_confirmation', 'Ожидает подтверждения'),
       ('accepted', 'Принят модератором'),
       ('under_delivery', 'В процессе доставки'),
       ('delivered', 'Доставлен');
