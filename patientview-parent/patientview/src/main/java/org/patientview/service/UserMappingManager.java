package org.patientview.service;

import org.patientview.model.Patient;
import org.patientview.patientview.model.User;
import org.patientview.patientview.model.UserMapping;

import java.util.List;

/**
 * Created by james@solidstategroup.com on 26/03/2014.
 */
public interface UserMappingManager {


    public void createPatientMapping(User user, Patient patient);

    public List<UserMapping> getAllByNhsNo(String nhsNo);

}
