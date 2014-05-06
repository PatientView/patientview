package org.patientview.repository.impl;

import org.patientview.patientview.model.Conversation;
import org.patientview.patientview.model.Message;
import org.patientview.patientview.model.SharedThought;
import org.patientview.patientview.model.SharedThoughtAudit;
import org.patientview.patientview.model.User;
import org.patientview.patientview.model.UserSharedThought;
import org.patientview.patientview.model.enums.ConversationType;
import org.patientview.patientview.model.enums.SharedThoughtAuditAction;
import org.patientview.repository.AbstractHibernateDAO;
import org.patientview.repository.SharedThoughtAuditDao;
import org.patientview.repository.SharedThoughtDao;
import org.patientview.service.SecurityUserManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Transactional(propagation = Propagation.MANDATORY)
@Repository(value = "sharedThoughtDao")
public class SharedThoughtDaoImpl extends AbstractHibernateDAO<SharedThought> implements SharedThoughtDao {

    @Inject
    private SharedThoughtAuditDao sharedThoughtAuditDao;
    @Inject
    private SecurityUserManager securityUserManager;

    @Override
    public boolean checkUserViewedThought(SharedThought sharedThought, User user) {

        for (UserSharedThought userSharedThought : sharedThought.getResponders()) {
            if (userSharedThought.getUser().equals(user) && userSharedThought.isViewed()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setUnviewed(SharedThought sharedThought, User ignoreUser) {
        for (UserSharedThought userSharedThought : sharedThought.getResponders()) {
            if (!userSharedThought.getUser().equals(ignoreUser)) {
                userSharedThought.setViewed(false);
            }
        }

        getEntityManager().merge(sharedThought);
        getEntityManager().flush();
    }

    @Override
    public void setViewed(SharedThought sharedThought, User user) {
        for (UserSharedThought userSharedThought : sharedThought.getResponders()) {
            if (userSharedThought.getUser().equals(user)) {
                userSharedThought.setViewed(true);
            }
        }

        getEntityManager().merge(sharedThought);
        getEntityManager().flush();
    }

    @Override
    public List<SharedThought> getAll(boolean orderBySubmitDate) {
        StringBuilder queryText = new StringBuilder();
        queryText.append("FROM      SharedThought ");

        if (orderBySubmitDate) {
            queryText.append("ORDER BY submitDate DESC");
        } else {
            queryText.append("ORDER BY dateLastSaved DESC");
        }

        TypedQuery<SharedThought> query = getEntityManager().createQuery(queryText.toString(), SharedThought.class);

        try {
            return query.getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<SharedThought> getUsersThoughts(User user, boolean isSubmitted) {
        StringBuilder queryText = new StringBuilder();
        queryText.append("FROM      SharedThought ");
        queryText.append("WHERE     user = :user ");
        queryText.append("AND       isSubmitted = :isSubmitted ");

        if (isSubmitted) {
            queryText.append("ORDER BY submitDate DESC ");
        } else {
            queryText.append("ORDER BY dateLastSaved DESC ");
        }

        TypedQuery<SharedThought> query = getEntityManager().createQuery(queryText.toString(), SharedThought.class);
        query.setParameter("user", user);
        query.setParameter("isSubmitted", isSubmitted);

        try {
            return query.getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<SharedThought> getStaffThoughtList(User user, boolean unViewedOnly) {
        // only show shared thought if user is member of shared thought's unit and user is in either the list of
        // shared thought responders or is a shared thought administrator for that unit
        StringBuilder queryText = new StringBuilder();
        queryText.append("SELECT    sth ");
        queryText.append("FROM      User AS usr ");
        queryText.append(",         UserMapping AS ump ");
        queryText.append(",         Unit AS uni ");
        queryText.append(",         SharedThought AS sth ");
        queryText.append(",         UserSharedThought AS ust ");
        queryText.append("WHERE     ump.username = usr.username ");
        queryText.append("AND       ump.unitcode = uni.unitcode ");
        queryText.append("AND       uni.sharedThoughtEnabled = true ");
        queryText.append("AND       ump.unitcode = sth.unit.unitcode ");
        queryText.append("AND       :username = usr.username ");

        // check user is in list of responders
        queryText.append("AND       (((");
        queryText.append("SELECT ust2 FROM UserSharedThought ust2 WHERE ust2.user = usr AND ust2.sharedThought = sth ");
        if (unViewedOnly) { queryText.append("AND ust2.viewed = false "); }
        queryText.append(") MEMBER OF sth.responders) ");

        // or is a sharedThoughtAdministrator (todo: null check required for correct sql)
        queryText.append("          OR (usr.sharedThoughtAdministrator = null)) ");
        queryText.append("GROUP BY  sth.id ");
        queryText.append("ORDER BY  sth.dateLastSaved DESC ");

        TypedQuery<SharedThought> query = getEntityManager().createQuery(queryText.toString(), SharedThought.class);
        query.setParameter("username", user.getUsername());

        try {
            return query.getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
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
        queryText.append("AND       ((usr.sharedThoughtResponder = true) OR (usr.sharedThoughtAdministrator = true)) ");
        queryText.append("AND       ump.unitcode = uni.unitcode ");
        queryText.append("AND       uni.sharedThoughtEnabled = true ");
        queryText.append("AND       ump.unitcode = :unitCode ");
        queryText.append("GROUP BY  usr.id");

        TypedQuery<User> query = getEntityManager().createQuery(queryText.toString(), User.class);
        query.setParameter("unitCode", sharedThought.getUnit().getUnitcode());

        try {
            List<User> users = query.getResultList();

            // remove existing responders from list
            for (UserSharedThought userSharedThought : sharedThought.getResponders()) {
                users.remove(userSharedThought.getUser());
            }

            return users;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean addResponder(SharedThought sharedThought, User responder) {

        try {
            UserSharedThought responderSharedThought = new UserSharedThought(responder, sharedThought, false);
            sharedThought.getResponders().add(responderSharedThought);
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
            StringBuilder queryText = new StringBuilder();
            queryText.append("SELECT    ust ");
            queryText.append("FROM      UserSharedThought AS ust ");
            queryText.append("WHERE     ust.user = :user ");
            queryText.append("AND       ust.sharedThought = :sharedThought ");
            TypedQuery<UserSharedThought> query
                    = getEntityManager().createQuery(queryText.toString(), UserSharedThought.class);
            query.setParameter("user", responder);
            query.setParameter("sharedThought", sharedThought);

            UserSharedThought userSharedThoughtToRemove = query.getSingleResult();
            sharedThought.getResponders().remove(userSharedThoughtToRemove);
            getEntityManager().remove(userSharedThoughtToRemove);
            getEntityManager().merge(sharedThought);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void addAllSharedThoughtAdministrators(SharedThought sharedThought) {
        // get list of current responders where usr.sharedThoughtAdministrator is true
        List<User> currentAdministrators = getCurrentSharedThoughtAdministrators(sharedThought);

        // get list of all usr.sharedThoughtAdministrator for this thought's unit
        List<User> possibleAdministrators = getPossibleSharedThoughtAdministrators(sharedThought);

        // remove existing from possible list and add to thought
        possibleAdministrators.removeAll(currentAdministrators);

        for (User user : possibleAdministrators) {
            UserSharedThought responderSharedThought = new UserSharedThought(user, sharedThought, false);
            sharedThought.getResponders().add(responderSharedThought);

            // audit
            SharedThoughtAudit audit = new SharedThoughtAudit();
            audit.setSharedThought(sharedThought);
            audit.setDate(new Date());
            audit.setAction(SharedThoughtAuditAction.ADD_RESPONDER);
            audit.setResponder(user);
            audit.setUser(securityUserManager.getLoggedInUser());
            sharedThoughtAuditDao.save(audit);
        }

        getEntityManager().merge(sharedThought);
        getEntityManager().flush();
    }

    private List<User> getCurrentSharedThoughtAdministrators(SharedThought sharedThought) {
        List<User> currentAdministrators = Collections.emptyList();
        for (UserSharedThought userSharedThought : sharedThought.getResponders()) {
            User user = userSharedThought.getUser();
            if (user.isSharedThoughtAdministrator()) {
                currentAdministrators.add(user);
            }
        }
        return currentAdministrators;
    }


    private List<User> getPossibleSharedThoughtAdministrators(SharedThought sharedThought) {
        StringBuilder queryText = new StringBuilder();
        queryText.append("SELECT    usr ");
        queryText.append("FROM      User AS usr ");
        queryText.append(",         UserMapping AS ump ");
        queryText.append(",         Unit AS uni ");
        queryText.append("WHERE     ump.username = usr.username ");
        queryText.append("AND       usr.sharedThoughtAdministrator = true ");
        queryText.append("AND       ump.unitcode = uni.unitcode ");
        queryText.append("AND       uni.sharedThoughtEnabled = true ");
        queryText.append("AND       ump.unitcode = :unitCode ");
        queryText.append("GROUP BY  usr.id");

        TypedQuery<User> query = getEntityManager().createQuery(queryText.toString(), User.class);
        query.setParameter("unitCode", sharedThought.getUnit().getUnitcode());

        try {
            return query.getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Message createSharedThoughtMessage(SharedThought sharedThought, String content, User sender) {
        try {
            if (sharedThought.getConversation() == null) {
                Conversation conversation = new Conversation();
                conversation.setParticipant1(sender);
                conversation.setParticipant2(sender);
                conversation.setSubject(ConversationType.SHARED_THOUGHT.toString());
                conversation.setType(ConversationType.SHARED_THOUGHT);
                conversation.setStarted(new Date());
                sharedThought.setConversation(conversation);
                sharedThought.setDateLastSaved(new Date());
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

            return message;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public boolean checkAccessSharingThoughts(User user) {
        StringBuilder queryText = new StringBuilder();
        queryText.append("SELECT    usr ");
        queryText.append("FROM      User AS usr ");
        queryText.append(",         UserMapping AS ump ");
        queryText.append(",         Unit AS uni ");
        queryText.append("WHERE     ump.username = usr.username ");
        queryText.append("AND       ump.unitcode = uni.unitcode ");
        queryText.append("AND       uni.sharedThoughtEnabled = true ");
        queryText.append("AND       usr = :user ");
        queryText.append("GROUP BY  usr.id ");

        TypedQuery<User> query = getEntityManager().createQuery(queryText.toString(), User.class);
        query.setParameter("user", user);

        try {
            return !query.getResultList().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean openCloseSharedThought(SharedThought sharedThought) {
        try {
            SharedThoughtAudit audit = new SharedThoughtAudit();
            audit.setSharedThought(sharedThought);
            audit.setDate(new Date());
            audit.setUser(securityUserManager.getLoggedInUser());

            if (sharedThought.getClosed()) {
                sharedThought.setClosed(false);
                audit.setAction(SharedThoughtAuditAction.OPEN);
            } else {
                sharedThought.setClosed(true);
                audit.setAction(SharedThoughtAuditAction.CLOSE);
            }

            sharedThoughtAuditDao.save(audit);

        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
