/* Sharing Thoughts */
CREATE TABLE `sharedthought` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `unit_id` bigint(20) NOT NULL,
  `conversation_id` bigint(20) DEFAULT NULL,
  `positive_negative` int(11) NOT NULL,
  `is_patient` tinyint(1) DEFAULT '0',
  `is_principal_carer` tinyint(1) NOT NULL DEFAULT '0',
  `is_relative` tinyint(1) NOT NULL DEFAULT '0',
  `is_friend` tinyint(1) NOT NULL DEFAULT '0',
  `is_about_me` tinyint(1) DEFAULT '0',
  `is_about_other` tinyint(1) DEFAULT '0',
  `is_anonymous` tinyint(1) DEFAULT '0',
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `is_ongoing` tinyint(1) DEFAULT '0',
  `location` text,
  `description` text,
  `suggested_action` text,
  `concern_reason` text,
  `likelihood_recurrence` int(11) DEFAULT NULL,
  `how_serious` int(11) DEFAULT NULL,
  `is_submitted` tinyint(1) NOT NULL DEFAULT '0',
  `submit_date` datetime DEFAULT NULL,
  `date_last_saved` datetime DEFAULT NULL,
  `allocation_user_id` bigint(20) DEFAULT NULL,
  `is_action_taken` tinyint(1) NOT NULL DEFAULT '0',
  `filterer_user_id` bigint(20) DEFAULT NULL,
  `is_patient_contacted` tinyint(1) NOT NULL DEFAULT '0',
  `notes` text,
  `status` text,
  `is_viewed` tinyint(1) NOT NULL DEFAULT '0',
  `closed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_sharedthought` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `sharedthought_id` bigint(20) NOT NULL,
  `viewed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `sharedthought_id` (`sharedthought_id`),
  CONSTRAINT `user_sharedthought_ibfk_1` FOREIGN KEY (`sharedthought_id`) REFERENCES `sharedthought` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `sharedthought_audit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sharedthought_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `message_id` bigint(20) DEFAULT NULL,
  `responder_id` bigint(20) DEFAULT NULL,
  `date` datetime NOT NULL,
  `action` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sharedthought_id` (`sharedthought_id`),
  CONSTRAINT `sharedthought_audit_ibfk_1` FOREIGN KEY (`sharedthought_id`) REFERENCES `sharedthought` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE unit ADD COLUMN `sharedThoughtEnabled` tinyint(1) DEFAULT '0';
ALTER TABLE user ADD COLUMN `sharedThoughtAdministrator` tinyint(1) NOT NULL DEFAULT '0';
ALTER TABLE user ADD COLUMN `sharedThoughtResponder` tinyint(1) NOT NULL DEFAULT '0';

  ALTER TABLE conversation ADD COLUMN `participant1Anonymous` tinyint(1) DEFAULT '0';
  ALTER TABLE conversation ADD COLUMN `participant2Anonymous` tinyint(1) DEFAULT '0';

