CREATE TABLE enterprises (
  id BIGSERIAL PRIMARY KEY,
  uuid VARCHAR(64) UNIQUE,
  address TEXT NOT NULL,
  timezone VARCHAR (255),
  communication_channel _VARCHAR,
  company VARCHAR (255) NOT NULL,
  cnpj VARCHAR (14) NOT NULL UNIQUE,
  status VARCHAR(40) DEFAULT 'enabled',
  corporate_reason VARCHAR (255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);