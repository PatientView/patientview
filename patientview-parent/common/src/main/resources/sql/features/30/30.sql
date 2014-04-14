

ALTER TABLE conversation ADD COLUMN `imageData` mediumtext;
ALTER TABLE conversation ADD COLUMN `rating` float DEFAULT NULL;
ALTER TABLE conversation ADD COLUMN `status` int(11) DEFAULT NULL;

CREATE TABLE `conversation_status` (
  `id` bigint(20) NOT NULL,
  `status` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

INSERT INTO conversation_status (id, status) VALUES (1, 'Open'), (2, 'Closed'), (3, 'Resolved');