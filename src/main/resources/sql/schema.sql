DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS group_permission CASCADE;
DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS permissions CASCADE;

CREATE TABLE permissions
(
    id   bigint      NOT NULL,
    name varchar(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE groups
(
    id   bigint      NOT NULL,
    name varchar(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE group_permission
(
    id            bigint NOT NULL,
    group_id      bigint NOT NULL,
    permission_id bigint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unq_group_id_permission_id UNIQUE (group_id, permission_id),
    CONSTRAINT fk_group_id_for_group_permission FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_permission_id_for_group_permission FOREIGN KEY (permission_id) REFERENCES permissions (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE users
(
    id       bigint      NOT NULL,
    login_id varchar(20) NOT NULL,
    passwd   varchar(80) NOT NULL,
    group_id bigint      NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unq_login_id UNIQUE (login_id),
    CONSTRAINT fk_group_id_for_user FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);