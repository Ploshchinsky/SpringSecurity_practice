CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(30) NOT NULL UNIQUE,
                       password VARCHAR(80) NOT NULL
);

CREATE TABLE roles (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL
);

CREATE TABLE users_roles (
                             user_id BIGINT NOT NULL,
                             role_id INT NOT NULL,
                             PRIMARY KEY (user_id, role_id),
                             FOREIGN KEY (user_id) REFERENCES users (id),
                             FOREIGN KEY (role_id) REFERENCES roles (id)
);

INSERT INTO roles (name) VALUES
                             ('ROLE_USER'),
                             ('ROLE_AUTHENTICATED'),
                             ('ROLE_ADMIN');