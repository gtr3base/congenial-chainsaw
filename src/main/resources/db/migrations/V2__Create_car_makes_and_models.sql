SET search_path TO av_schema, public;

CREATE TABLE car_makes(
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE car_models(
                           id SERIAL PRIMARY KEY,
                           make_id INT REFERENCES car_makes(id) ON DELETE CASCADE,
                           name VARCHAR(50) NOT NULL,
                           UNIQUE(make_id, name)
);