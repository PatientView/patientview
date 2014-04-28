
ALTER TABLE unit ADD COLUMN `feedbackEnabled` tinyint(1) DEFAULT '0';

ALTER TABLE conversation ADD COLUMN `imageData` mediumtext;
ALTER TABLE conversation ADD COLUMN `rating` float DEFAULT NULL;
ALTER TABLE conversation ADD COLUMN `status` int(11) DEFAULT NULL;
ALTER TABLE conversation ADD COLUMN `clinicianClosed` tinyint(1) DEFAULT '0';

CREATE TABLE `conversation_status` (
  `id` bigint(20) NOT NULL,
  `status` text NOT NULL,
  `closedStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

INSERT INTO conversation_status (id, status, closedStatus) VALUES (1, 'Open - Ongoing', 0), (2, 'Closed - Amended', 1), (3, 'Closed - Explained Records', 1), (4, 'Closed - Will Discuss in Clinic', 1);

ALTER TABLE user ADD COLUMN `feedbackRecipient` tinyint(1) NOT NULL DEFAULT '0';

UPDATE conversation SET type='MESSAGE' WHERE type IS NULL;
