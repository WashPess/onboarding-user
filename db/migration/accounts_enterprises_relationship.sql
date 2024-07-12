CREATE TABLE accounts_enterprises_relationship (
  account_uuid VARCHAR (64) NOT NULL,
  enterprise_uuid VARCHAR(64) NOT NULL
);

ALTER TABLE accounts_enterprises_relationship ADD CONSTRAINT fk_account_accounts_enterprises_relationship FOREIGN KEY (account_uuid) REFERENCES accounts(uuid);
ALTER TABLE accounts_enterprises_relationship ADD CONSTRAINT fk_enterprise_accounts_enterprises_relationship FOREIGN KEY (enterprise_uuid) REFERENCES enterprises(uuid);