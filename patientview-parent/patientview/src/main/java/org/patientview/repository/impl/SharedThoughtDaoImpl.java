package org.patientview.repository.impl;

import org.patientview.patientview.model.SharedThought;
import org.patientview.patientview.model.SharedThought_;
import org.patientview.patientview.model.User;
import org.patientview.repository.AbstractHibernateDAO;
import org.patientview.repository.SharedThoughtDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Transactional(propagation = Propagation.MANDATORY)
@Repository(value = "sharedThoughtDao")
public class SharedThoughtDaoImpl extends AbstractHibernateDAO<SharedThought> implements SharedThoughtDao {

    @Override
    public SharedThought get(Long id) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SharedThought> criteria = builder.createQuery(SharedThought.class);

        Root<SharedThought> root = criteria.from(SharedThought.class);

        List<Predicate> wherePredicates = new ArrayList<Predicate>();

        wherePredicates.add(builder.equal(root.get(SharedThought_.id), id));

        buildWhereClause(criteria, wherePredicates);

        try {
            return getEntityManager().createQuery(criteria).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<SharedThought> getUsersThoughts(Long userId, boolean isSubmitted) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SharedThought> criteria = builder.createQuery(SharedThought.class);

        Root<SharedThought> root = criteria.from(SharedThought.class);

        List<Predicate> wherePredicates = new ArrayList<Predicate>();

        wherePredicates.add(builder.equal(root.get(SharedThought_.user), userId));
        wherePredicates.add(builder.equal(root.get(SharedThought_.isSubmitted), isSubmitted));

        buildWhereClause(criteria, wherePredicates);

        try {
            return getEntityManager().createQuery(criteria).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<User> getOtherResponders(SharedThought sharedThought) {
        StringBuilder queryText = new StringBuilder();
        queryText.append("SELECT    usr ");
        queryText.append("FROM      User AS usr ");
        queryText.append(",         UserMapping AS ump ");
        queryText.append(",         Unit AS uni ");
        queryText.append("WHERE     ump.username = usr.username ");
        queryText.append("AND       ump.unitcode = uni.unitcode ");
        queryText.append("AND       ump.unitcode = :unitCode ");
        queryText.append("GROUP BY  usr.id");

        TypedQuery<User> query = getEntityManager().createQuery(queryText.toString(), User.class);
        query.setParameter("unitCode", sharedThought.getUnit().getUnitcode());

        try {
            List<User> users = query.getResultList();
            users.removeAll(sharedThought.getResponders());
            return users;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean addResponder(SharedThought sharedThought, User responder) {

        try {
            sharedThought.getResponders().add(responder);
            getEntityManager().merge(sharedThought);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean removeResponder(SharedThought sharedThought, User responder) {

        try {
            sharedThought.getResponders().remove(responder);
            getEntityManager().merge(sharedThought);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
