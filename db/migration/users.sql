CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  uuid VARCHAR(64),
  email VARCHAR (40),
  document VARCHAR (30),
  lastname VARCHAR (30),
  nickname VARCHAR(30),
  password VARCHAR(40),
  firstname VARCHAR (30),
  confirm_password VARCHAR (40)
);