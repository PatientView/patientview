

ALTER TABLE conversation ADD COLUMN `imageData` mediumtext;
ALTER TABLE conversation ADD COLUMN `rating` float DEFAULT NULL;
ALTER TABLE conversation ADD COLUMN `status` int(11) DEFAULT NULL;
ALTER TABLE conversation ADD COLUMN `clinicianClosed` tinyint(1) DEFAULT '0';

CREATE TABLE `conversation_status` (
  `id` bigint(20) NOT NULL,
  `status` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

INSERT INTO conversation_status (id, status) VALUES (1, 'Closed - Amended Records'), (2, 'Closed - Explained Records'), (3, 'Closed - Will Discuss in Clinic');