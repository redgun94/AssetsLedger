


CREATE TABLE company (
                               id           UUID PRIMARY KEY,
                               company_nmls VARCHAR(20) UNIQUE,
                               company_name VARCHAR(64) NOT NULL,
                               company_type VARCHAR(20) NOT NULL,
                               street      VARCHAR(150),
                               city        VARCHAR(100),
                               state       VARCHAR(2),
                               zip_code    VARCHAR(10),
                               created_at  TIMESTAMP    NOT NULL DEFAULT now()
);
ALTER TABLE loan_officers
    ADD COLUMN company_id UUID  REFERENCES company(id);

CREATE INDEX idx_loan_officers_company_id ON loan_officers(company_id);