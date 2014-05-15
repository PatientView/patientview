package org.patientview.service.impl;

import org.patientview.patientview.model.SpecialtyUserRole;
import org.patientview.patientview.model.User;
import org.patientview.repository.SpecialtyUserRoleDao;
import org.patientview.service.SpecialtyUserRoleManager;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by jameseaton@solidstategroup.com on 14/05/2014.
 */
@Service
public class SpecialtyUserRoleManagerImpl implements SpecialtyUserRoleManager {

    @Inject
    private SpecialtyUserRoleDao specialtyUserRoleDao;

    public void save(SpecialtyUserRole specialtyUserRole) {
        specialtyUserRoleDao.save(specialtyUserRole);
    }


    public List<SpecialtyUserRole> get(User user) {
        return specialtyUserRoleDao.get(user);
    }
}
