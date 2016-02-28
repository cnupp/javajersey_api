update solutions set name = 'api' where name = 'data api';
update solutions set name = 'spa' where name = 'single page app';
insert into stacks(name, description) values ('react', 'react redux and webpack');