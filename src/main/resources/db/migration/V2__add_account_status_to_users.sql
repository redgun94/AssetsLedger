ALTER TABLE users ADD COLUMN account_status VARCHAR(20);

-- Optional: Set a default value for existing rows so NOT NULL doesn't fail
UPDATE users SET account_status = 'ACTIVE' WHERE account_status IS NULL;

-- Make the column NOT NULL after populating existing data
ALTER TABLE users ALTER COLUMN account_status SET NOT NULL;

alter table users
    alter column id type uuid using id::uuid;