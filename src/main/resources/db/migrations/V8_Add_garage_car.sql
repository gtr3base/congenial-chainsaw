SET search_path TO av_schema, public;

CREATE TABLE garage_car(
    id BIGSERIAL PRIMARY KEY,
    model_id INT REFERENCES car_models(id) ON DELETE RESTRICT,
    generation_id INT REFERENCES car_generations(id) ON DELETE SET NULL,
    year INT NOT NULL CHECK (year >= 1886),
    price DECIMAL(10, 2) NOT NULL CHECK(price > 0),
    vin_code VARCHAR(17) NOT NULL,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    garage_id INT NOT NULL REFERENCES garage(id) ON DELETE CASCADE
);