-- member schema
DROP TABLE IF EXISTS member CASCADE;

create table member (member_id bigint not null, login_id varchar(255), password varchar(255), primary key (member_id));

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

-- article index
create index article_idx_writer_id on article (writer_id)
create index article_idx_category_code on article (category_code)
create index article_idx_trade_status_code on article (trade_status_code)
create index article_idx_writer_id_trade_status_code on article (writer_id, trade_status_code)

-- product_image schema
DROP TABLE IF EXISTS product_image CASCADE;

create table product_image (
                               product_image_id bigint not null,
                               image_url varchar(255),
                               article_id bigint,
                               primary key (product_image_id)
)

-- like_article 테이블
DROP TABLE IF EXISTS like_article CASCADE;

create table like_article (
    like_article_id bigint not null,
    article_id bigint,
    member_id bigint,
    primary key (like_article_id)
)

-- like_article_index 인덱스
create index like_article_idx_member_id_article_id on like_article (member_id, article_id)
create index like_article_idx_member_id on like_article (member_id)

-- offer 테이블
DROP TABLE IF EXISTS offer CASCADE;

create table offer (
    offer_id bigint not null,
    created_date timestamp,
    is_selected tinyint(1),
    price integer,
    article_id bigint,
    offerer_id bigint,
    primary key (offer_id)
)

-- offer table index
create index offer_idx_offerer_id_is_Selected on offer (offerer_id, is_selected)
create index offer_idx_article_id on offer (article_id)

-- review table index
drop table if exists review CASCADE

create table review (
    review_id bigint not null,
    content TEXT,
    created_date timestamp,
    score integer,
    article_id bigint,
    reviewee_id bigint,
    reviewer_id bigint,
    primary key (review_id)
)

-- review table index
create index review_idx_reviewee_id on review (reviewee_id)
create index review_idx_reviewer_id_article_id on review (reviewer_id, article_id)

-- FK 설정
-- 1. review 테이블
ALTER TABLE review ADD FOREIGN KEY (article_id) REFERENCES article(article_id);
ALTER TABLE review ADD FOREIGN KEY (from_member_id) REFERENCES member(member_id);
ALTER TABLE review ADD FOREIGN KEY (to_member_id) REFERENCES member(member_id);

-- 2. article 테이블
ALTER TABLE article ADD FOREIGN KEY (writer_id) REFERENCES member(member_id);

-- 3. like_article 테이블
ALTER TABLE like_article ADD FOREIGN KEY (article_id) REFERENCES article(article_id);
ALTER TABLE like_article ADD FOREIGN KEY (member_id) REFERENCES member(member_id);

-- 4. offer 테이블
ALTER TABLE offer ADD FOREIGN KEY (offerer_id) REFERENCES member(member_id);
ALTER TABLE offer ADD FOREIGN KEY (article_id) REFERENCES article(article_id);

-- 5. product_image 테이블
ALTER TABLE product_image ADD FOREIGN KEY (article_id) REFERENCES article(article_id);