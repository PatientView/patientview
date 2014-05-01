package org.patientview.repository.impl;

import org.patientview.patientview.model.SharedThoughtAudit;
import org.patientview.repository.AbstractHibernateDAO;
import org.patientview.repository.SharedThoughtAuditDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.MANDATORY)
@Repository(value = "sharedThoughtAuditDao")
public class SharedThoughtAuditDaoImpl
        extends AbstractHibernateDAO<SharedThoughtAudit> implements SharedThoughtAuditDao {

}
