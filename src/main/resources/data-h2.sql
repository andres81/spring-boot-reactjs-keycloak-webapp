INSERT INTO user_account(id, email, password, is_admin)
VALUES(1, 'as', '$2a$10$Bb3gXDAZ/5WMcQjCV1Ns/.Gx5ZpJ8tyxkwibYU9rHB3Pu1VqTsfxu', 1);

INSERT INTO user_status(user_account_id, account_status)
VALUES(1, 'ACTIVE');