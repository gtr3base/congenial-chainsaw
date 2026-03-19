SET search_path TO av_schema, public;

CREATE TABLE garage(
    id BIGSERIAL PRIMARY KEY,
    user_id INT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    locked BOOLEAN DEFAULT FALSE
);

CREATE TABLE note(
    id BIGSERIAL PRIMARY KEY,
    title TEXT,
    text TEXT,
    content BYTEA,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)