CREATE TABLE loan_officers (
                               id           UUID PRIMARY KEY,
                               user_id      UUID NOT NULL UNIQUE REFERENCES users(id),
                               nmls         VARCHAR(20) UNIQUE,
                               officer_type VARCHAR(20) NOT NULL
);