CREATE TABLE USERS (
  ID  SERIAL NOT NULL,
  NAME VARCHAR(100) NOT NULL,
  EMAIL VARCHAR(255) NOT NULL,
  PASSWORD VARCHAR(100) NOT NULL,
  ADMIN_CODE INTEGER DEFAULT 0,
  ADDRESS VARCHAR(255) DEFAULT NULL,
  PHONE VARCHAR(50) DEFAULT NULL,
  CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

INSERT INTO USERS (NAME, EMAIL, PASSWORD, ADMIN_CODE, ADDRESS, PHONE) VALUES
('admin', 'admin@example.com','admin1234', 1, '東京都品川区1-1', '090-0000-0000'),
('test1', 'test1@example.com','test1234', 0, '東京都荒川区1-1', '090-0000-0000'),
('test2', 'test2@example.com','test1234', 0, '東京都渋谷区1-1', '090-0000-0000');





--postgresの場合(パスワード暗号化のパターン)
INSERT INTO users (name, email, password, admin_code) VALUES
('admin', 'admin@example.com','$2y$10$/Egm530y0MFUx0q3/TTWveUVId94DF1PUtuzaEzdm0W0kQ.2C1Joa', 1),
('About_me', 'aboutme@example.com','$2y$10$QqzU.DDGSwaYqcovF0w0vuiA4ebiQNZuHDe5wBae56S4U5ep1tuBW', 9),
('guest', 'guest@example.com','$2y$10$ehiNFPkWhE/tKThn.o/y8OVpGxTAqVrcsxhMoSjVb8SdLpedTzcR.', 0),
('link', 'link@example.com','$2y$10$QqzU.DDGSwaYqcovF0w0vuiA4ebiQNZuHDe5wBae56S4U5ep1tuBW', 8);







--mysqlの場合
CREATE TABLE `sampledb`.`USERS` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `address` VARCHAR(255) NULL,
  `phone` VARCHAR(50) NULL,
  `update_date` DATETIME NOT NULL,
  `create_date` DATETIME NOT NULL,
  `delete_date` DATETIME NULL,
  PRIMARY KEY (`id`));

INSERT INTO `sampledb`.`user` (`id`, `name`, `address`, `phone`, `update_date`, `create_date`) VALUES ('1', 'テスト太郎', '東京都品川区1-1', '090-0000-0000', '2021/06/30', '2021/06/30');
INSERT INTO `sampledb`.`user` (`id`, `name`, `address`, `phone`, `update_date`, `create_date`) VALUES ('2', 'テスト次郎', '東京都渋谷区1-1', '080-0000-0000', '2021/06/30', '2021/06/30');
