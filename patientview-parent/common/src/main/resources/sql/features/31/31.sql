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

CREATE TABLE specialty_result_heading (id INT(20) PRIMARY KEY AUTO_INCREMENT,
                                       result_heading_id BIGINT(20),
                                       heading VARCHAR(30),
                                       rollover VARCHAR(50),
                                       panel int(11),
                                       panelorder int(11),
                                       specialty_id int(20));

INSERT INTO specialty_result_heading(result_heading_id, heading, rollover, panel, panelorder, specialty_id)
  SELECT  id
    ,       heading
    ,       rollover
    ,       panel
    ,       panelorder
    ,       1
  FROM    result_heading;


ALTER TABLE result_heading DROP COLUMN panel;

ALTER TABLE result_heading DROP COLUMN panelorder;

ALTER TABLE result_heading DROP COLUMN specialty_id;



INSERT INTO edtacode(edtacode, linktype, description, specialty_id) VALUES ('static','static','Further Information', 3);

DROP INDEX `edtacode` ON edtacode;

CREATE INDEX `edtacode_idx` ON edtacode (edtacode, specialty_id);


CREATE TABLE `dia_careplan` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nhsno` varchar(255) NOT NULL,
  `diabetesOverallScore` int(11) DEFAULT NULL,
  `moodScore` int(11) DEFAULT NULL,
  `diabetesControlScore` int(11) DEFAULT NULL,
  `hypoglycaemiaScore` int(11) DEFAULT NULL,
  `weightScore` int(11) DEFAULT NULL,
  `bloodPressureScore` int(11) DEFAULT NULL,
  `cholesterolScore` int(11) DEFAULT NULL,
  `smokingAlcoholScore` int(11) DEFAULT NULL,
  `eatingExerciseSportScore` int(11) DEFAULT NULL,
  `drivingWorkStudyScore` int(11) DEFAULT NULL,
  `pregnancySexRelationshipsScore` int(11) DEFAULT NULL,
  `eyesScore` int(11) DEFAULT NULL,
  `kidneysScore` int(11) DEFAULT NULL,
  `feetScore` int(11) DEFAULT NULL,
  `heartAttacksStrokesScore` int(11) DEFAULT NULL,
  `takingMedicationRegularlyScore` int(11) DEFAULT NULL,
  `otherAreasToDiscuss` text,
  `goals` text,
  `goalToAchieve` text,
  `importance_id` bigint(20) NOT NULL,
  `howToAchieveGoal` text,
  `barriers` text,
  `whatCanBeDone` text,
  `confidence_id` bigint(20) NOT NULL,
  `reviewDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE `dia_eyecheckup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nhsno` varchar(255) NOT NULL DEFAULT '',
  `unitcode` varchar(255) NOT NULL DEFAULT '',
  `lastRetinalDate` datetime DEFAULT NULL,
  `lastRetinalPlace` varchar(255) DEFAULT NULL,
  `leftMGrade` varchar(255) DEFAULT NULL,
  `leftRGrade` varchar(255) DEFAULT NULL,
  `leftVA` varchar(255) DEFAULT NULL,
  `rightMGrade` varchar(255) DEFAULT NULL,
  `rightRGrade` varchar(255) DEFAULT NULL,
  `rightVA` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eye_date_nhsno_unitcode` (`lastRetinalDate`,`nhsno`,`unitcode`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `dia_mydiabetes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nhsno` varchar(255) NOT NULL,
  `unitcode` varchar(255) DEFAULT NULL,
  `diabetesType` varchar(255) DEFAULT NULL,
  `yearDiagnosed` int(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `patient` (`nhsno`,`unitcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `dia_footcheckup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nhsno` varchar(255) NOT NULL DEFAULT '',
  `unitcode` varchar(255) NOT NULL DEFAULT '',
  `footCheckDate` datetime DEFAULT NULL,
  `footCheckPlace` varchar(255) DEFAULT NULL,
  `leftDpPulse` varchar(255) DEFAULT NULL,
  `leftPtPulse` varchar(255) DEFAULT NULL,
  `leftMonofilament` varchar(255) DEFAULT NULL,
  `leftRiskScore` varchar(255) DEFAULT NULL,
  `rightDpPulse` varchar(255) DEFAULT NULL,
  `rightPtPulse` varchar(255) DEFAULT NULL,
  `rightMonofilament` varchar(255) DEFAULT NULL,
  `rightRiskScore` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_foot_date_nhsno_unitcode` (`footCheckDate`,`nhsno`,`unitcode`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;