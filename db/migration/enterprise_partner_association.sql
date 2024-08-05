CREATE TABLE enterprise_partner_association (
    id BIGSERIAL PRIMARY KEY,
    enterprise_uuid VARCHAR(64) NOT NULL,
    partner_uuid VARCHAR(64) NOT NULL
);

ALTER TABLE enterprise_partner_association ADD CONSTRAINT fk_enterpise_enterprise_partner_association FOREIGN KEY (enterprise_uuid) REFERENCES enterprises(uuid);
ALTER TABLE enterprise_partner_association ADD CONSTRAINT fk_partner_enterprise_partner_association FOREIGN KEY (partner_uuid) REFERENCES partners(uuid);