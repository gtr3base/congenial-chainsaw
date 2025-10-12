SET search_path TO av_schema, public;

CREATE TABLE price_tracking(
                               id SERIAL PRIMARY KEY,
                               car_id INT REFERENCES cars(id) ON DELETE CASCADE,
                               price DECIMAL(10, 2) NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE favorites (
                           user_id INT REFERENCES users(id) ON DELETE CASCADE,
                           car_id INT REFERENCES cars(id) ON DELETE CASCADE,
                           added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           PRIMARY KEY (user_id, car_id)
);