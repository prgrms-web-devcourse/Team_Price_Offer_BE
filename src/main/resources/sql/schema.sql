DROP TABLE IF EXISTS members CASCADE;

create table members (member_id bigint not null, login_id varchar(255), password varchar(255), primary key (member_id));

alter table members add constraint UK_lq5wej6688i1bd6b5c11neptj unique (login_id);