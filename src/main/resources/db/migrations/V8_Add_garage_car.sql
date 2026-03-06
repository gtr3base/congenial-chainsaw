SET search_path TO av_schema, public;

CREATE TABLE garage_car(
    id BIGSERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    garage_id INT NOT NULL REFERENCES garage(id) ON DELETE CASCADE
);