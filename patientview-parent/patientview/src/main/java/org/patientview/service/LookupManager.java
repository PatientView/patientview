package org.patientview.service;

import org.patientview.model.LookupType;
import org.patientview.model.LookupValue;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by james@solidstategroup.com on 14/04/2014.
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface LookupManager {

    LookupType get(String name);

    List<LookupValue> getByComponent(String name);

}
