package org.patientview.repository;

import org.patientview.patientview.model.SharedThoughtAudit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.MANDATORY)
public interface SharedThoughtAuditDao {

    void save(SharedThoughtAudit audit);
}
