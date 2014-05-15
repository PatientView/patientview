package org.patientview.service;

import org.patientview.patientview.model.SpecialtyUserRole;
import org.patientview.patientview.model.User;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by james@solidstategroup.com on 14/05/2014.
 */
@Transactional(propagation = Propagation.REQUIRED)
@Secured(value = { "IS_AUTHENTICATED_ANONYMOUSLY" })
public interface SpecialtyUserRoleManager {

    void save(SpecialtyUserRole specialtyUserRole);

    List<SpecialtyUserRole> get(User user);
}
