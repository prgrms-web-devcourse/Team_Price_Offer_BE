INSERT INTO permissions(id, name)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN')
;

INSERT INTO groups(id, name)
VALUES (1, 'USER_GROUP'),
       (2, 'ADMIN_GROUP')
;

-- USER_GROUP (ROLE_USER)
-- ADMIN_GROUP (ROLE_USER, ROLE_ADMIN)
INSERT INTO group_permission(id, group_id, permission_id)
VALUES (1, 1, 1),
       (2, 2, 1),
       (3, 2, 2)
;

-- user 비밀번호 : user123
-- admin 비밀번호 : admin123
INSERT INTO users(id, login_id, passwd, group_id)
VALUES (1, 'user', '$2a$10$B32L76wyCEGqG/UVKPYk9uqZHCWb7k4ci98VTQ7l.dCEib/kzpKGe', 1),
       (2, 'admin', '$2a$10$/enTGRjB6noB9NCd8g5kGuLchiTsZsqcUyXkUn4yglUPZ4WZ9MvrK', 2)
;