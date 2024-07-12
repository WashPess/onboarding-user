CREATE TYPE status AS ENUM ('enabled', 'disabled');

CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  uuid VARCHAR(64) UNIQUE, 
  email VARCHAR (40) UNIQUE,
  document VARCHAR (30) UNIQUE,
  last_name VARCHAR (30),
  nickname VARCHAR(30),
  password VARCHAR(40),
  full_name VARCHAR (70),
  first_name VARCHAR (30),
  optin BOOLEAN,
  status VARCHAR(40),
  created_at TIMESTAMP not null default CURRENT_TIMESTAMP,
  updated_at TIMESTAMP not null default CURRENT_TIMESTAMP
);