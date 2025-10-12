SET search_path TO av_schema, public;

CREATE TABLE cars (
                      id SERIAL PRIMARY KEY,
                      user_id INT REFERENCES users(id) ON DELETE CASCADE,
                      generation_id INT REFERENCES car_generations(id) ON DELETE SET NULL,
                      year INT,
                      price DECIMAL(10, 2) NOT NULL,
                      description TEXT,
                      status car_status DEFAULT 'PENDING',
                      vin_code VARCHAR(17) UNIQUE,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);