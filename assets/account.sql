

CREATE TYPE GENDER AS ENUM ('male', 'female');

CREATE TABLE account (
	marital VARCHAR(255),
	nickname VARCHAR(255),
	birthday DATE,
	currency VARCHAR(255),
	language VARCHAR(255),
	rg VARCHAR(255),
	cpf VARCHAR(255),
	uuid VARCHAR(255),
	nationality VARCHAR(255),
	picture_part VARCHAR(255),
	dicument VARCHAR(255),
	optin  BOOLEAN NOT NULL,
	gender GENDER,
	cretaed_at DATE NOT NULL DEFAULT CURRENT_DATE,
	update_at DATE NOT NULL DEFAULT CURRENT_DATE
)