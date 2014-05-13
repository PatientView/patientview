package org.patientview.service.impl;

import org.patientview.model.LookupType;
import org.patientview.model.LookupValue;
import org.patientview.repository.LookupDao;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by james@solidstategroup.com on 14/04/2014.
 */
@Service
public class LookupManagerImpl implements org.patientview.service.LookupManager {

    @Inject
    private LookupDao lookupDao;

    @Override
    public LookupType get(String name) {
        return lookupDao.get(name);
    }

    @Override
    public List<LookupValue> getByComponent(String name) {
        return lookupDao.getByComponent(name);
    }

}
