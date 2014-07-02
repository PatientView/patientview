/* example patient delete script, with nhsno */
DELETE FROM testresult WHERE nhsno = '1111111111';
DELETE FROM usermapping WHERE nhsno = '1111111111';
DELETE FROM uktstatus WHERE nhsno = '1111111111'; 
DELETE FROM treatment WHERE nhsno = '1111111111';
DELETE FROM pv_user_log WHERE nhsno = '1111111111';
DELETE FROM pv_procedure WHERE nhsno = '1111111111';
DELETE FROM pv_patientjoin_request WHERE nhsno = '1111111111';
DELETE FROM pv_allergy WHERE nhsno = '1111111111';
DELETE FROM patient WHERE nhsno = '1111111111';
DELETE FROM medicine WHERE nhsno = '1111111111';
DELETE FROM LOG WHERE nhsno = '1111111111';
DELETE FROM letter WHERE nhsno = '1111111111';
DELETE FROM ibd_nutrition WHERE nhsno = '1111111111';
DELETE FROM ibd_myibd_severity_level WHERE nhsno = '1111111111';
DELETE FROM ibd_myibd WHERE nhsno = '1111111111';
DELETE FROM ibd_my_medication WHERE nhsno = '1111111111';
DELETE FROM ibd_crohns_symptoms WHERE nhsno = '1111111111';
DELETE FROM ibd_colitis_symptoms WHERE nhsno = '1111111111';
DELETE FROM ibd_careplan WHERE nhsno = '1111111111';
DELETE FROM feedback WHERE nhsno = '1111111111';
DELETE FROM diagnostic WHERE nhsno = '1111111111';
DELETE FROM diagnosis WHERE nhsno = '1111111111';
DELETE FROM comment WHERE nhsno = '1111111111';
DELETE FROM aboutme WHERE nhsno = '1111111111';

/* and username */
DELETE FROM emailverification WHERE username = 'USERNAME';
DELETE FROM feedback WHERE username = 'USERNAME';
DELETE FROM splashpageuserseen WHERE username = 'USERNAME';
DELETE FROM user WHERE username = 'USERNAME';
DELETE FROM usermapping WHERE username = 'USERNAME';
