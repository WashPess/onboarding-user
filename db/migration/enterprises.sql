CREATE TABLE enterprises (
  id BIGSERIAL PRIMARY KEY,
  uuid VARCHAR(64) UNIQUE,
  site VARCHAR (255),
  company VARCHAR (255),
  timezone VARCHAR (255),
  professional VARCHAR (255),
  communication_channel _VARCHAR,
  created_at TIMESTAMP not null default CURRENT_TIMESTAMP,
  updated_at TIMESTAMP not null default CURRENT_TIMESTAMP
);