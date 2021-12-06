DROP TABLE IF EXISTS members CASCADE;

create table members (member_id bigint not null, login_id varchar(255), password varchar(255), primary key (member_id));