ALTER TABLE `npu`.`users`
  ADD INDEX `role_id_idx` (`role_id` ASC) VISIBLE;
;
ALTER TABLE `npu`.`users`
  ADD CONSTRAINT `role_id`
    FOREIGN KEY (`role_id`)
      REFERENCES `npu`.`roles` (`id`)
      ON DELETE RESTRICT
      ON UPDATE RESTRICT;