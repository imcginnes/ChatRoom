drop table messages;
drop table contacts;
drop table user;

CREATE TABLE user
(username varchar (25),
password varbinary (16));

CREATE TABLE contacts
(username varchar (25),
contactName varchar (25));

CREATE TABLE messages
(mfrom varchar (25),
mto varchar (25),
message varchar (50),
dt datetime);

alter table user
 add constraint user_username_pk primary key(username);
alter table contacts
 add constraint contacts_username_contactName_pk primary key(username,contactName);
alter table messages
 add constraint messages_from_to_dt_pk primary key(mfrom,mto,dt);

alter table contacts
 add constraint contacts_contactName_fk foreign key(contactName)
 references user(username);
alter table messages
 add constraint messages_from_fk foreign key(mfrom)
 references user(username);
alter table messages
 add constraint messages_to_fk foreign key(mto)
 references user(username);