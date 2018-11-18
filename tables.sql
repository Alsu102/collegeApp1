CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `firstname` varchar(20) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `group_id` int(11) DEFAULT '3',
  `lastname` varchar(45) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `address` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
INSERT INTO `cas`.`users` (`id`, `firstname`, `email`, `group_id`, `lastname`, `phone`, `address`) VALUES ('815175', 'name', 'name@name.com', '3', 'lastname', '1234567891', 'bfhdbvjs ');



CREATE TABLE `groups` (
  `id` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `access` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

#second table for groups
INSERT INTO `cas`.`groups` (`id`, `name`, `access`) VALUES ('1', 'Admin', '255');
INSERT INTO `cas`.`groups` (`id`, `name`, `access`) VALUES ('2', 'Guest', '0');
INSERT INTO `cas`.`groups` (`id`, `name`, `access`) VALUES ('3', 'Student', '16');
INSERT INTO `cas`.`groups` (`id`, `name`, `access`) VALUES ('4', 'Instructors', '128');


