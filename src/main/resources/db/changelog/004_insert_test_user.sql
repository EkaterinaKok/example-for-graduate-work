-- liquibase formatted sql
-- changeset EAKok:004
INSERT INTO users (username, first_name, last_name, password_hash, role)
VALUES ('test_user', 'Test', 'User', '$2a$10$3Y.8xaNf1ZpJth1vcBMWSuFhPBymOSPv9Z4wOyBzsa1IyI.hg4/ZS', 'USER');

-- Логин: test_user  Пароль: password