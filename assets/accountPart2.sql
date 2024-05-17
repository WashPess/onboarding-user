
CREATE TYPE GENDER AS ENUM ('Male', 'Female');

CREATE TABLE accountPart2 (
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    cpf VARCHAR (255),
    whathsapp VARCHAR (14),
    link VARCHAR (255),
    uuid VARCHAR (255),
    birthday DATE,
    gender GENDER,
    currency VARCHAR (255),
    language VARCHAR (255),
    nationality VARCHAR (255),
    optin BOOLEAN NOT NULL,
    cretaed_at DATE NOT NULL DEFAULT CURRENT_DATE,
    upgrate_at DATE NOT NULL DEFAULT CURRENT_DATE
)


