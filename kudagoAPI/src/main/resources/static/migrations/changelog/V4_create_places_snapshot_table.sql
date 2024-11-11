CREATE TABLE places_snapshot (
                        id SERIAL PRIMARY KEY,
                        slug VARCHAR(255) NOT NULL,
                        name VARCHAR(255) NOT NULL,
                        created_at DATE NOT NULL,
                        timezone VARCHAR(255) NOT NULL,
                        language VARCHAR(255) NOT NULL
)