-- members schema
DROP TABLE IF EXISTS members CASCADE;

create table members (member_id bigint not null, login_id varchar(255), password varchar(255), primary key (member_id));

alter table members add constraint UK_lq5wej6688i1bd6b5c11neptj unique (login_id);

-- article schema
DROP TABLE IF EXISTS article CASCADE;

create table article (
                         article_id bigint not null,
                         category_code integer,
                         content TEXT,
                         created_date timestamp,
                         like_count integer,
                         main_image_url TEXT,
                         modified_date timestamp,
                         price integer,
                         product_status_code integer,
                         quantity integer,
                         title varchar(255),
                         trade_area varchar(255),
                         trade_method_code integer,
                         trade_status_code integer,
                         view_count integer,
                         writer_id bigint,
                         primary key (article_id)
)

-- product_image schema
DROP TABLE IF EXISTS article CASCADE;

create table product_image (
                               product_image_id bigint not null,
                               image_url varchar(255),
                               article_id bigint,
                               primary key (product_image_id)
)