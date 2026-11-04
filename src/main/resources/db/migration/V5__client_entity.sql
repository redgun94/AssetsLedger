CREATE TABLE clients (
                         id              UUID PRIMARY KEY,
                         loan_officer_id UUID         NOT NULL REFERENCES loan_officers(id),
                         email           VARCHAR(150) NOT NULL,
                         first_name      VARCHAR(120) NOT NULL,
                         last_name       VARCHAR(150) NOT NULL,
                         phone           VARCHAR(15)  NOT NULL,
                         service_status  VARCHAR(20)  NOT NULL,
                         created_at      TIMESTAMP    NOT NULL DEFAULT now(),
                         CONSTRAINT uq_clients_loan_officer_email UNIQUE (loan_officer_id, email)
);