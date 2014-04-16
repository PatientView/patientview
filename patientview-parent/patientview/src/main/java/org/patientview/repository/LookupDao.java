package org.patientview.repository;

import org.patientview.model.LookupType;
import org.patientview.model.LookupValue;

import java.util.List;

/**
 * Created by james@solidstategroup.com on 14/04/2014.
 */
public interface LookupDao {

    LookupType get(String name);

    List<LookupValue> getByComponent(String name);

}
