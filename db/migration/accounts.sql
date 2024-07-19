CREATE TABLE accounts (
  id BIGSERIAL PRIMARY KEY,
  uuid VARCHAR(64) UNIQUE, 
  user_uuid VARCHAR(64) UNIQUE, 
  rg VARCHAR(30),
  gender VARCHAR (30),
  marital VARCHAR (30),
  currency VARCHAR (30),
  language VARCHAR (30),
  nationality VARCHAR (30),
  optin BOOLEAN DEFAULT FALSE,
  nickname VARCHAR(30) UNIQUE NOT NULL,
  document VARCHAR(14) UNIQUE NOT NULL,
  created_at TIMESTAMP not null default CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE accounts ADD CONSTRAINT fk_uuid_users_accounts FOREIGN KEY (user_uuid) REFERENCES users(uuid);