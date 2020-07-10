drop table if exists reminder;

create table reminder
(
  reminder_id bigint auto_increment,
  user_id bigint,
  message varchar (255),
  date date,
  time time,
  primary key (reminder_id)
);
