package org.patientview.repository.impl;

import org.patientview.model.LookupType;
import org.patientview.model.LookupValue;
import org.patientview.repository.AbstractHibernateDAO;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

/**
 *
 * Created by james@solidstategroup.com on 14/04/2014.
 */
@Repository(value = "lookupDao")
public class LookupDaoImpl extends AbstractHibernateDAO<LookupType> implements org.patientview.repository.LookupDao {

    @Override
    public LookupType get(String name) {

        StringBuilder hsql = new StringBuilder();

        hsql.append("SELECT    lt ");
        hsql.append("FROM     LookupType lt ");
        hsql.append("WHERE    lt.name = :name ");

       // hsql.append("ORDER BY lv.value");

        Query query = getEntityManager().createQuery(hsql.toString(), LookupType.class);
        query.setParameter("name", name);

        return (LookupType) query.getSingleResult();

    }

    @Override
    public List<LookupValue> getByComponent(String name) {

        StringBuilder hsql = new StringBuilder();

        hsql.append("SELECT   lv ");
        hsql.append("FROM     LookupValue lv ");
        hsql.append("JOIN     lv.lookupType lt ");
        hsql.append("WHERE    lt.componentName = :name ");
        hsql.append("ORDER BY lv.value");

        Query query = getEntityManager().createQuery(hsql.toString(), LookupValue.class);
        query.setParameter("name", name);

        return query.getResultList();

    }

}
