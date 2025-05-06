USE bl8mhtvdutiek9bkcnfx;

DROP TABLE IF EXISTS user_activation_codes;
DROP TABLE IF EXISTS users_roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS games;
DROP TABLE IF EXISTS publishers;
DROP TABLE IF EXISTS roles;

CREATE TABLE roles (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       role VARCHAR(255)
);

CREATE TABLE publishers (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL UNIQUE,
                            thumbnail_url TEXT NOT NULL
);

CREATE TABLE games (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL UNIQUE,
                       description TEXT NOT NULL,
                       price DECIMAL(19,2) NOT NULL,
                       genre VARCHAR(255) NOT NULL,
                       thumbnail_url TEXT NOT NULL,
                       publisher_id BIGINT NOT NULL,
                       CONSTRAINT fk_games_publisher FOREIGN KEY (publisher_id) REFERENCES publishers(id)
);

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       is_active BOOLEAN,
                       CHECK (is_active IN (0,1))
);

CREATE TABLE users_roles (
                             user_id BIGINT NOT NULL,
                             role_id BIGINT NOT NULL,
                             PRIMARY KEY (user_id, role_id),
                             CONSTRAINT fk_users_roles_user FOREIGN KEY (user_id) REFERENCES users(id),
                             CONSTRAINT fk_users_roles_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE user_activation_codes (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       activation_code VARCHAR(255),
                                       created TIMESTAMP,
                                       user_id BIGINT,
                                       CONSTRAINT fk_activation_user FOREIGN KEY (user_id) REFERENCES users(id)
);
