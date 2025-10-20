SET search_path TO av_schema, public;

CREATE TABLE cars (
                      id SERIAL PRIMARY KEY,
                      user_id INT REFERENCES users(id) ON DELETE CASCADE,
                      generation_id INT REFERENCES car_generations(id) ON DELETE SET NULL,
                      year INT CHECK (
                          year BETWEEN (
                              SELECT year_start FROM car_generations WHERE id = generation_id
                          ) AND (
                              SELECT COALESCE(year_end, EXTRACT(YEAR FROM CURRENT_DATE))
                              FROM car_generations WHERE id = generation_id
                          )
                      ),
                      price DECIMAL(10, 2) NOT NULL CHECK(price > 0),
                      description TEXT,
                      status car_status DEFAULT 'PENDING',
                      vin_code VARCHAR(17) NOT NULL,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);