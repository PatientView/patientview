/**
    Add columns in user table.
 */
ALTER TABLE user ADD COLUMN `created` datetime DEFAULT NULL;
ALTER TABLE user ADD COLUMN `updated` datetime DEFAULT NULL;

/**
    Update the user' created from log table.
 */
UPDATE user u INNER JOIN log
ON (
        u.username = log.user
    AND
        (
            log.action = 'patient add'
         OR
            log.action = 'admin add'
         )
    )
SET u.created = log.date;



ALTER TABLE pv_groupmessage ADD UNIQUE uniquegroupmessage (conversation_id, recipient_id);

ALTER TABLE user ADD COLUMN 'accounthidden' tinyint(1) NOT NULL DEFAULT '0';
ALTER TABLE result_heading ADD COLUMN 'minRangeValue' float DEFAULT NULL;
ALTER TABLE result_heading ADD COLUMN 'maxRangeValue' float DEFAULT NULL;
ALTER TABLE result_heading ADD COLUMN 'units' varchar(255) DEFAULT NULL;
