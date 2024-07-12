CREATE TABLE accounts (
  id BIGSERIAL PRIMARY KEY,
  uuid VARCHAR(64) UNIQUE, 
  user_uuid VARCHAR(64) UNIQUE, 
  document VARCHAR(30) UNIQUE,
  nickname VARCHAR(30),
  rg VARCHAR(30),
  gender VARCHAR (30),
  language VARCHAR (30),
  marital VARCHAR (30),
  nationality VARCHAR (30),
  currency VARCHAR (30),
  created_at TIMESTAMP not null default CURRENT_TIMESTAMP,
  updated_at TIMESTAMP not null default CURRENT_TIMESTAMP,
  optin BOOLEAN
);

ALTER TABLE accounts ADD CONSTRAINT fk_uuid_users_accounts FOREIGN KEY (user_uuid) REFERENCES users(uuid);