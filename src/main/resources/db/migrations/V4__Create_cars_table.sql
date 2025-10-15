SET search_path TO av_schema, public;

CREATE TABLE cars (
                      id SERIAL PRIMARY KEY,
                      user_id INT REFERENCES users(id) ON DELETE CASCADE,
                      generation_id INT REFERENCES car_generations(id) ON DELETE SET NULL,
                      year INT CHECK(year >= 1900 AND year <= EXTRACT(YEAR FROM CURRENT_DATE) + 1),
                      price DECIMAL(10, 2) NOT NULL CHECK(price > 0),
                      description TEXT,
                      status car_status DEFAULT 'PENDING',
                      vin_code VARCHAR(17) UNIQUE,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);