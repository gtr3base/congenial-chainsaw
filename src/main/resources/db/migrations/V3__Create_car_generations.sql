SET search_path TO av_schema, public;

CREATE TABLE car_generations(
                                id SERIAL PRIMARY KEY,
                                model_id INT REFERENCES car_models(id) ON DELETE CASCADE,
                                name VARCHAR(50) NOT NULL,
                                UNIQUE(model_id, name)
);