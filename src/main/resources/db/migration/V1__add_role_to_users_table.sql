-- 1. Users table mein role column add karein
ALTER TABLE users
    ADD COLUMN role VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER';

-- 2. Purane existing users ka role backfill/update karein
UPDATE users
SET role = 'ROLE_USER'
WHERE role IS NULL OR role = '';

# 3. Specific user (Rehan) ko ADMIN banayein
UPDATE users
SET role = 'ROLE_ADMIN'
WHERE email = 'rehan@gmail.com';