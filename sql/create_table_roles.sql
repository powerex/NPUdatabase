CREATE TABLE `npu`.`roles` (
                             `id` INT NOT NULL AUTO_INCREMENT,
                             `name` VARCHAR(45) NOT NULL,
                             PRIMARY KEY (`id`),
                             UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;
