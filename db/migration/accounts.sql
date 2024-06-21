CREATE TABLE accounts (
  id BIGSERIAL PRIMARY KEY,
  uuid VARCHAR(64) UNIQUE, 
  document VARCHAR(30) UNIQUE,
  nickname VARCHAR(30),
  rg VARCHAR(30),
  gender VARCHAR (30),
  language VARCHAR (30),
  marital VARCHAR (30),
  nationality VARCHAR (30),
  optin BOOLEAN
);