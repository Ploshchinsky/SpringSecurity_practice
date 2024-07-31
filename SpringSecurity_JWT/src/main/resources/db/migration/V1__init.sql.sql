create table users
(
    id             bigserial,
    username       varchar(30) not null unique,
    password       varchar(80) not null,
    email          varchar(50) unique,
    failed_attempts integer,  -- Новое поле
    blocking_time  timestamp, -- Новое поле
    primary key (id)
);

create table roles
(
    id   serial,
    name varchar(50) not null,
    primary key (id)
);

CREATE TABLE users_roles
(
    user_id bigint not null,
    role_id int    not null,
    primary key (user_id, role_id),
    foreign key (user_id) references users (id),
    foreign key (role_id) references roles (id)
);

insert into roles (name)
values ('ROLE_USER'),
       ('ROLE_SUPER_ADMIN'),
       ('ROLE_MODERATOR');

insert into users (username, password, email, failed_attempts, blocking_time)
values ('user', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'user@gmail.com', 0, null),
       ('admin', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'admin@gmail.com', 0, null),
       ('moderator', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'moderator@gmail.com', 0, null);

insert into users_roles (user_id, role_id)
values (1, 1),
       (2, 2),
       (3, 3);
