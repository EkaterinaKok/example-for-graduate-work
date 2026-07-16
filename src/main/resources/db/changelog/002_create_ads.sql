-- liquibase formatted sql
-- changeset EAKok:002
CREATE TABLE IF NOT EXISTS ads (
    pk SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    price INTEGER NOT NULL CHECK (price >= 0),
    image_url VARCHAR(500),
    author_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ads_author FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);