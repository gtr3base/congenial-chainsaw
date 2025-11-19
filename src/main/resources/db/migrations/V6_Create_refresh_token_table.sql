SET search_path TO av_schema, public;

CREATE TABLE refresh_token (
        id BIGSERIAL PRIMARY KEY,
        token VARCHAR NOT NULL UNIQUE,
        expiry_date TIMESTAMPTZ NOT NULL,
        user_id INT REFERENCES users(id) ON DELETE CASCADE
)