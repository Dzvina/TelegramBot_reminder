drop table if exists reminder;

create table reminder
(
  reminder_id int,
  chat_id bigint,
  message varchar (255),
  date date,
  time time
);
