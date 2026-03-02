SET search_path TO av_schema, public;

CREATE TABLE garage_car(
    id BIGSERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    car_id INT NOT NULL REFERENCES cars(id) ON DELETE CASCADE
);