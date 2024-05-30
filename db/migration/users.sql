CREATE TABLE users (
  uuid VARCHAR(64) PRIMARY KEY,
  email VARCHAR (40) UNIQUE,
  document VARCHAR (30) UNIQUE,
  last_name VARCHAR (30),
  nickname VARCHAR(30),
  password VARCHAR(40),
  full_name VARCHAR (70),
  first_name VARCHAR (30),
  optin BOOLEAN 
);