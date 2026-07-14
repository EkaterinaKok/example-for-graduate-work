-- liquibase formatted sql
-- changeset EAKok:003
CREATE TABLE IF NOT EXISTS comments (
    pk SERIAL PRIMARY KEY,
    text VARCHAR(64) NOT NULL,
    author_id INTEGER NOT NULL,
    ad_id INTEGER NOT NULL,
    created_at BIGINT NOT NULL, -- Unix timestamp в мс
    CONSTRAINT fk_comments_author FOREIGN KEY (author_id) REFERENCES users(id),
    CONSTRAINT fk_comments_ad FOREIGN KEY (ad_id) REFERENCES ads(pk) ON DELETE CASCADE
);