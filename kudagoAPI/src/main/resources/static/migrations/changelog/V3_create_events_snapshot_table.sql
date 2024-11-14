CREATE TABLE events_snapshot (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL ,
                        created_at DATE NOT NULL,
                        date DATE NOT NULL,
                        place_id INTEGER
);