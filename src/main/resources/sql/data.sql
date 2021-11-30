-- user 비밀번호 : user123
-- admin 비밀번호 : admin123
INSERT INTO members(member_id, login_id, password)
VALUES (1, 'user', '$2a$10$Hs.dTjUn.gvXDIwGpJeGsuWEp4IZTIGEPpiiFwe.MOzCuX5K.MIQ2'),
       (2, 'admin', '$2a$10$cseJCUXQl3aIefHp2RNv4ONxJ6GwsTxcwe/oatWgNUwCKZ/9djNVS')
;