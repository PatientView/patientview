package org.patientview.repository.impl;

import org.patientview.patientview.model.Conversation;
import org.patientview.patientview.model.Message;
import org.patientview.patientview.model.SharedThought;
import org.patientview.patientview.model.SharedThought_;
import org.patientview.patientview.model.User;
import org.patientview.patientview.model.enums.ConversationType;
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
import java.util.Date;
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
    public List<SharedThought> getAll() {
        StringBuilder queryText = new StringBuilder();
        queryText.append("FROM      SharedThought ");
        queryText.append("ORDER BY  dateLastSaved DESC");

        TypedQuery<SharedThought> query = getEntityManager().createQuery(queryText.toString(), SharedThought.class);

        try {
            return query.getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
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
        queryText.append("AND       usr.isclinician = true ");
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

    @Override
    public boolean createSharedThoughtMessage(SharedThought sharedThought, String content, User sender) {
        try {
            if (sharedThought.getConversation() == null) {
                Conversation conversation = new Conversation();
                conversation.setParticipant1(sender);
                conversation.setParticipant2(sender);
                conversation.setSubject(ConversationType.SHARED_THOUGHT.toString());
                conversation.setType(ConversationType.SHARED_THOUGHT);
                conversation.setStarted(new Date());
                sharedThought.setConversation(conversation);
                getEntityManager().persist(conversation);
                getEntityManager().merge(sharedThought);
                getEntityManager().flush();
            }

            Message message = new Message();
            message.setConversation(sharedThought.getConversation());
            message.setSender(sender);
            message.setContent(content);
            message.setType(ConversationType.SHARED_THOUGHT);
            message.setDate(new Date());
            getEntityManager().persist(message);
            getEntityManager().flush();

            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
