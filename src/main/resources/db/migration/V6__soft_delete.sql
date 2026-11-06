ALTER TABLE users ADD COLUMN deleted_at TIMESTAMP;
ALTER TABLE users ADD COLUMN deleted_email VARCHAR(255);
ALTER TABLE loan_officers ADD COLUMN deleted_nmls VARCHAR(255);
UPDATE users SET account_status = 'SUSPENDED' WHERE account_status = 'SUSPENSE';

