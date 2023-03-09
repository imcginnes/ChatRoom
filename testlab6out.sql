insert into user
 values('jsmith@uca.edu',aes_encrypt('hello123','key'));
insert into user
 values('msmith@uca.edu',aes_encrypt('pass123','key'));
insert into user
 values('tjones@yahoo.com',aes_encrypt('123456','key'));
insert into user
 values('jjones@yahoo.com',aes_encrypt('hello1234','key'));

insert into contacts
 values('jsmith@uca.edu','msmith@uca.edu');
insert into contacts
 values('jsmith@uca.edu','tjones@yahoo.com');
insert into contacts
 values('msmith@uca.edu','tjones@yahoo.com');

insert into messages
 values('jsmith@uca.edu','msmith@uca.edu','Hello',now());
insert into messages
 values('tjones@yahoo.com','msmith@uca.edu','How r u?',now());
insert into messages
 values('tjones@yahoo.com','jsmith@uca.edu','Hello Joe',now());

select * from user;
select aes_decrypt(password,'key') from user;
select * from contacts;
select * from messages;