/* ECR additions */
ALTER TABLE `user` ADD COLUMN `ecrOptOutNotNow` tinyint(1) DEFAULT '0';
ALTER TABLE `user` ADD COLUMN `ecrOptInNotNow` tinyint(1) DEFAULT '0';

/* ECR unit */
INSERT INTO `unit` (`unitcode`, `name`, `shortname`, `sourceType`, `specialty_id`, `visible`, `feedbackEnabled`, `sharedThoughtEnabled`, `ecrEnabled`)
VALUES ('ECS', 'ECS', 'ECS', 'renalunit', 1, 0, 0, 0, 0);
