--update persons set personid =3 where city ='Rio de Janeiro' and firstname ='Jumecinto' and personid >2 and personid <=4;
--delete from persons where personid is null
--ALTER TABLE peoples ADD Id serial NOT null primary key;
--ALTER TABLE peoples ADD personsid int;
--ALTER TABLE identities ADD CONSTRAINT fk_identities_peoples FOREIGN KEY (people_id) REFERENCES peoples (id);
--ALTER TABLE peoples RENAME COLUMN personsid TO personid;
--ALTER TABLE peoples drop column id cascade;
--CREATE TABLE IDENTITIES ( person_id int, people_id int);
select * from peoples order by id;
--insert into peoples (lastname, firstname, age) values ('Almeida', 'Alvaro', 99);
--ALTER TABLE identities ALTER COLUMN people_id set default 0;
--ALTER TABLE peoples ALTER COLUMN id type serial;
--update persons set city ='Sao Paulo' where id =3;
insert into identities (people_id, person_id) values (1, 2);
