DELETE FROM user_permission_join_table;
DELETE FROM user_token;
DELETE FROM user_status;
DELETE FROM user_account;

INSERT INTO user_account(id, email, password, is_admin) VALUES(1, 'as', '$2a$10$Bb3gXDAZ/5WMcQjCV1Ns/.Gx5ZpJ8tyxkwibYU9rHB3Pu1VqTsfxu', 1);
INSERT INTO user_account(id, email, password) VALUES(2, 'user1', '$2a$10$Bb3gXDAZ/5WMcQjCV1Ns/.Gx5ZpJ8tyxkwibYU9rHB3Pu1VqTsfxu');
INSERT INTO user_account(id, email, password) VALUES(3, 'user2', '$2a$10$Bb3gXDAZ/5WMcQjCV1Ns/.Gx5ZpJ8tyxkwibYU9rHB3Pu1VqTsfxu');
INSERT INTO user_account(id, email, password) VALUES(4, 'user3', '$2a$10$Bb3gXDAZ/5WMcQjCV1Ns/.Gx5ZpJ8tyxkwibYU9rHB3Pu1VqTsfxu');

INSERT INTO user_status(user_account_id, account_status) VALUES(1, 'ACTIVE');
INSERT INTO user_status(user_account_id, account_status) VALUES(2, 'ACTIVE');
INSERT INTO user_status(user_account_id, account_status) VALUES(3, 'ACTIVE');
INSERT INTO user_status(user_account_id, account_status) VALUES(4, 'ACTIVE');