CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  uuid VARCHAR(64) UNIQUE, 
  full_name VARCHAR (70),
  optin BOOLEAN DEFAULT FALSE,
  password VARCHAR(40) NOT NULL,
  last_name VARCHAR (30) NOT NULL,
  first_name VARCHAR (30) NOT NULL,
  email VARCHAR (60) UNIQUE NOT NULL,
  nickname VARCHAR(30) UNIQUE NOT NULL,
  status VARCHAR(40) DEFAULT 'enabled',
  document VARCHAR (14) UNIQUE NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Busca por: nome(full_name), documento(document), email(email), apelido(nickname)
-- SELECT id, uuid, full_name, optin, last_name, first_name, email, nickname, status, document, created_at FROM users WHERE full_name ILIKE '%term%' OR document ILIKE '%term%' OR email ILIKE '%term%' OR nickname ILIKE '%term%' ORDER BY full_name ASC;