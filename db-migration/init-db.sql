# Copyright 2018 Andre Schepers
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

CREATE DATABASE IF NOT EXISTS yourdatabase;

CREATE USER 'webapp'@'localhost' IDENTIFIED BY 'v;KfcPR/w8:C-+kt';

GRANT SELECT, INSERT, UPDATE, DELETE ON yourdatabase.* TO 'webapp'@'localhost';

USE yourdatabase;

CREATE TABLE IF NOT EXISTS user_account (
    id BIGINT NOT NULL AUTO_INCREMENT,
    fullname VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'fullname',
    password VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL,
    email VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL,
    is_admin TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY(id),
    UNIQUE(EMAIL)
);

CREATE TABLE IF NOT EXISTS user_token (
    id BIGINT NOT NULL AUTO_INCREMENT,
    token VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL,
    user_account_id BIGINT NOT NULL,
    FOREIGN KEY (user_account_id) REFERENCES user_account(id),
    PRIMARY KEY(id),
    CONSTRAINT uc_Token_UserId UNIQUE (user_account_id,token)
);

CREATE TABLE IF NOT EXISTS reset_password_token (
    id BIGINT NOT NULL AUTO_INCREMENT,
    token VARCHAR(36) CHARACTER SET utf8mb4 NOT NULL,
    user_account_id BIGINT NOT NULL,
    creation_datetime DATETIME NOT NULL,
    FOREIGN KEY (user_account_id) REFERENCES user_account(id),
    PRIMARY KEY(id),
    CONSTRAINT uc_Token_UserId UNIQUE (user_account_id,token)
);

CREATE TABLE IF NOT EXISTS user_status (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_account_id BIGINT NOT NULL,
    account_status VARCHAR(30) CHARACTER SET utf8mb4 NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (user_account_id) REFERENCES user_account(id),
    CONSTRAINT uc_UserId_Status UNIQUE (user_account_id,account_status)
);

CREATE TABLE IF NOT EXISTS user_pending_token (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_account_id BIGINT NOT NULL UNIQUE,
    token VARCHAR(36) CHARACTER SET utf8mb4 NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (user_account_id) REFERENCES user_account(id)
);

CREATE TABLE IF NOT EXISTS rest_resource_permission (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL UNIQUE,
  description VARCHAR(2000) CHARACTER SET utf8mb4,
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS user_permission_join_table (
  id BIGINT NOT NULL AUTO_INCREMENT,
  permission_id BIGINT NOT NULL,
  user_account_id BIGINT NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY (permission_id) REFERENCES rest_resource_permission(id),
  FOREIGN KEY (user_account_id) REFERENCES user_account(id),
  CONSTRAINT uc_PermissionId_UserId UNIQUE (user_account_id, permission_id)
);