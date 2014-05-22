ALTER TABLE sharedthought ADD COLUMN `is_other` tinyint(1) DEFAULT '0';
ALTER TABLE sharedthought ADD COLUMN `is_other_more` text;
ALTER TABLE sharedthought ADD COLUMN `likelihood_recurrence_more` text;