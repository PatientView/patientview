/* Sharing Thoughts additions */
ALTER TABLE `sharedthought` ADD COLUMN `is_about_other_non_patient` tinyint(1) DEFAULT '0';
ALTER TABLE `sharedthought` ADD COLUMN `is_about_other_non_patient_more` text;