/* ECR additions */
ALTER TABLE `user` ADD COLUMN `ecrOptOutNotNow` tinyint(1) DEFAULT '0';
ALTER TABLE `user` ADD COLUMN `ecrOptInNotNow` tinyint(1) DEFAULT '0';
