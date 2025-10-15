SET search_path TO av_schema, public;

CREATE TYPE user_role AS ENUM('USER', 'ADMIN');
CREATE TYPE car_status AS ENUM('PENDING', 'APPROVED', 'REJECTED');

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE CHECK (email *~ '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
                       password VARCHAR(255) NOT NULL,
                       role user_role NOT NULL DEFAULT 'GUEST',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);