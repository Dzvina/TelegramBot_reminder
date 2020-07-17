drop table if exists city;

create table city
(
  city_name varchar (255),
  longitude double,
  latitude double,
  country varchar (255)
);


select sysdate() from city;
