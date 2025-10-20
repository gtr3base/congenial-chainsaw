SET search_path TO av_schema, public;

CREATE TABLE car_generations(
                                id SERIAL PRIMARY KEY,
                                model_id INT REFERENCES car_models(id) ON DELETE CASCADE,
                                year_start INT NOT NULL,
                                year_end INT,
                                name VARCHAR(50) NOT NULL,
                                UNIQUE(model_id, name),
                                CONSTRAINT valid_year_range CHECK(
                                    year_end IS NULL OR year_end >= year_start
                                )
);