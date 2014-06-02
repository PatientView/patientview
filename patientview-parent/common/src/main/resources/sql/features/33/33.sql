/* ECS/SCS integration additions */
ALTER TABLE `user` ADD COLUMN  `ecrOptInStatus` tinyint(1) NOT NULL DEFAULT '0',
ALTER TABLE `user` ADD COLUMN  `ecrOptOutPermanently` tinyint(1) NOT NULL DEFAULT '0',