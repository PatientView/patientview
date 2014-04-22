package org.patientview.service.impl;

import org.patientview.model.Patient;
import org.patientview.model.Specialty;
import org.patientview.patientview.model.User;
import org.patientview.patientview.model.UserMapping;
import org.patientview.patientview.unit.UnitUtils;
import org.patientview.repository.UserMappingDao;
import org.patientview.service.UserManager;
import org.patientview.service.UserMappingManager;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by james@solidstategroup.com on 26/03/2014.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
@Secured(value = { "ROLE_ANY_USER" })
public class UserMappingManagerImpl implements UserMappingManager {

    @Inject
    private UserMappingDao userMappingDao;

    @Inject
    private UserManager userManager;

    /**
     * Create the required user mapping for a patient. Unfortunately we have to pass
     * the user and patient objects to create the mapping. Did not what form objecs in
     * this manager.
     *
     * @param user
     * @param patient
     */
    public void createPatientMapping(User user, Patient patient) {

        Specialty specialty = userManager.getCurrentSpecialty(userManager.getLoggedInUser());

        // create 2 new UserMapping objects, one with selected details and another
        // with generic PATIENT_ENTERS_UNITCODE unit code (currently user code "PATIENT")
        UserMapping userMapping = new UserMapping(user.getUsername(), patient.getUnitcode(),
                patient.getNhsno());
        userMapping.setSpecialty(specialty);
        UserMapping userMappingPatientEnters = new UserMapping(user.getUsername(),
                UnitUtils.PATIENT_ENTERS_UNITCODE, patient.getNhsno());
        userMappingPatientEnters.setSpecialty(specialty);
        UserMapping userMappingGp = new UserMapping(user.getUsername() + "-GP", patient.getUnitcode(),
                patient.getNhsno());
        userMappingGp.setSpecialty(specialty);

        userMappingDao.save(userMapping);
        userMappingDao.save(userMappingPatientEnters);
        userMappingDao.save(userMappingGp);

    }

    public List<UserMapping> getAllByNhsNo(String nhsNo) {
        return userMappingDao.getAllByNhsNo(nhsNo);
    }

}
