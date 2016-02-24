CREATE TABLE users(id BIGINT PRIMARY KEY auto_increment, username STRING(50), firstname STRING(20), lastname STRING(30), email STRING(50), phone STRING(15), password STRING(128));

CREATE TABLE ships(id INTEGER PRIMARY KEY, name STRING(50), type STRING(128), country STRING(4));

INSERT INTO ships VALUES(1, 'Dwight D. Eisenhower', 'Aircraft carrier', 'USA');
INSERT INTO ships VALUES(2, 'Carl Vinson', 'Aircraft carrier', 'GB');
INSERT INTO ships VALUES(3, 'Udaloy', 'Destroyer', 'RUS');
INSERT INTO ships VALUES(4, 'Kirov', 'Battlecruiser', 'RUS');

commit;

drop table sailors;
drop table ships;
CREATE TABLE sailors(id BIGINT PRIMARY KEY auto_increment, first_name varchar2(20), last_name varchar2(30), speciality varchar2(30),  hiredate DATETIME, ship_empl INTEGER);
CREATE TABLE ships(id BIGINT PRIMARY KEY auto_increment, name varchar2(50), type varchar2(128), country varchar2(4));
CREATE TABLE users(id BIGINT PRIMARY KEY auto_increment, login varchar2(50), first_name varchar2(20), last_name varchar2(30), email varchar2(50), phone varchar2(15), password varchar2(128));

---alter table sailors add foreign key (ship_empl) reference ships(id) on delete cascade;

INSERT INTO sailors VALUES(1, 'Valisyi', 'Bykov', 'Chief cook', '2010-01-04', 4);
INSERT INTO sailors VALUES(2, 'Yaroslav', 'Galych', 'Seaman', '2014-01-20', 4);
INSERT INTO sailors VALUES(3, 'John', 'Doe', 'Capitan', '2002-10-01', 1);
INSERT INTO sailors VALUES(4, 'Cavin', 'Lanister', 'Boatswain', '2010-06-06', 1);
INSERT INTO sailors VALUES(5, 'Mark', 'Brown', 'Physician', '2012-10-16', 2);
INSERT INTO sailors VALUES(6, 'Nick', 'Carroll', 'Seaman', '2003-01-09', 2);
INSERT INTO sailors VALUES(7, 'Eugene', 'Crownsberg', 'Captain', '2014-08-11', 3);
INSERT INTO sailors VALUES(8, 'Ulrich', 'Bloodaxe', 'Captain', '2005-03-12', 3);

INSERT INTO ships VALUES(1, 'Dwight D. Eisenhower', 'Aircraft carrier', 'USA');
INSERT INTO ships VALUES(2, 'Carl Vinson', 'Aircraft carrier', 'GB');
INSERT INTO ships VALUES(3, 'Udaloy', 'Destroyer', 'RUS');
INSERT INTO ships VALUES(4, 'Kirov', 'Battlecruiser', 'RUS');

INSERT INTO sailors VALUES(1, 'Valisyi', 'Bykov', 'Chief cook', '2010-01-04', 4);

commit;

disconnect;