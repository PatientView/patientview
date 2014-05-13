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


CREATE TABLE LOOKUP_VALUE (id BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
							lookup_type_id VARCHAR(200),
							lookup_value VARCHAR(200),
							lookup_text VARCHAR(500),
							created DATETIME);

CREATE TABLE LOOKUP_TYPE (id BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
						   lookup_name VARCHAR(50),
						   lookup_type VARCHAR(50),
						   component_name VARCHAR(50),
						   created DATETIME);
