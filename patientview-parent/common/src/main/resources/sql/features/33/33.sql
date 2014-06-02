/* ECS/SCS integration additions */
ALTER TABLE `unit` ADD COLUMN  `ecrEnabled` tinyint(1) NOT NULL DEFAULT '0';
ALTER TABLE `user` ADD COLUMN  `ecrOptOutPermanently` tinyint(1) NOT NULL DEFAULT '0';
ALTER TABLE `user` ADD COLUMN  `ecrOptOutPermanently` tinyint(1) NOT NULL DEFAULT '0';

INSERT INTO `user` (`username`, `password`, `email`, `created`)
VALUES ('ecr', '891e12e156d8c6609c6d5f3e04b2fc8da6d9ff3d7e9f906314c0909da69637eb', 'test@solidstategroup.com', NOW());