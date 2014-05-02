package org.patientview.service.impl;

import org.patientview.patientview.model.Message;
import org.patientview.patientview.model.SharedThought;
import org.patientview.patientview.model.SharedThoughtAudit;
import org.patientview.patientview.model.User;
import org.patientview.patientview.model.enums.SharedThoughtAuditAction;
import org.patientview.repository.SharedThoughtAuditDao;
import org.patientview.repository.SharedThoughtDao;
import org.patientview.repository.UserDao;
import org.patientview.service.SecurityUserManager;
import org.patientview.service.SharedThoughtManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@Service(value = "sharedThoughtManager")
public class SharedThoughtManagerImpl implements SharedThoughtManager {

    @Inject
    private SharedThoughtDao sharedThoughtDao;
    @Inject
    private SharedThoughtAuditDao sharedThoughtAuditDao;
    @Inject
    private UserDao userDao;
    @Inject
    private SecurityUserManager securityUserManager;

    @Override
    public List<SharedThought> getAll(boolean orderBySubmitDate) {
        return sharedThoughtDao.getAll(orderBySubmitDate);
    }

    @Override
    public SharedThought get(Long sharedThoughtId, boolean auditEnabled, boolean staffUser) {
        SharedThought thought = sharedThoughtDao.get(sharedThoughtId);

        // audit view/get
        if (auditEnabled) {
            SharedThoughtAudit audit = new SharedThoughtAudit();
            audit.setSharedThought(thought);
            audit.setDate(new Date());
            audit.setUser(securityUserManager.getLoggedInUser());

            if (staffUser) {
                audit.setAction(SharedThoughtAuditAction.STAFF_VIEW);
            } else {
                audit.setAction(SharedThoughtAuditAction.PATIENT_VIEW);
            }

            sharedThoughtAuditDao.save(audit);
        }

        return thought;
    }

    @Override
    public List<SharedThought> getUsersThoughts(User user, boolean isSubmitted) {
        return sharedThoughtDao.getUsersThoughts(user, isSubmitted);
    }

    @Override
    public List<SharedThought> getStaffThoughtList(User user, boolean unViewedOnly) {
        return sharedThoughtDao.getStaffThoughtList(user, unViewedOnly);
    }

    @Override
    public void delete(Long sharedThoughtId) {
        sharedThoughtDao.delete(sharedThoughtId);
    }

    @Override
    public void save(SharedThought thought, boolean isSubmitted) {
        thought.setDateLastSaved(new Date());
        User loggedInUser = securityUserManager.getLoggedInUser();

        // audit
        SharedThoughtAudit audit = new SharedThoughtAudit();
        audit.setSharedThought(thought);
        audit.setDate(new Date());
        audit.setUser(loggedInUser);

        if (isSubmitted) {
            thought.setSubmitDate(new Date());
            audit.setAction(SharedThoughtAuditAction.SUBMIT);
        } else {
            audit.setAction(SharedThoughtAuditAction.SAVE);
        }

        sharedThoughtDao.save(thought);
        sharedThoughtAuditDao.save(audit);
    }

    @Override
    public void setUnviewed(SharedThought sharedThought) {
        sharedThoughtDao.setUnviewed(sharedThought, securityUserManager.getLoggedInUser());
    }

    @Override
    public void setViewed(SharedThought sharedThought, User user) {
        sharedThoughtDao.setViewed(sharedThought, user);
    }

    @Override
    public List<User> getOtherResponders(SharedThought sharedThought) {
        return sharedThoughtDao.getOtherResponders(sharedThought);
    }

    @Override
    public boolean addResponder(Long sharedThoughtId, Long responderId) {
        SharedThought sharedThought = get(sharedThoughtId, false, true);
        User responder = userDao.get(responderId);

        if (sharedThought != null && responder != null) {

            // audit
            SharedThoughtAudit audit = new SharedThoughtAudit();
            audit.setSharedThought(sharedThought);
            audit.setDate(new Date());
            audit.setAction(SharedThoughtAuditAction.ADD_RESPONDER);
            audit.setResponder(responder);
            audit.setUser(securityUserManager.getLoggedInUser());
            sharedThoughtAuditDao.save(audit);

            return sharedThoughtDao.addResponder(sharedThought, responder);
        } else {
            return false;
        }
    }

    @Override
    public boolean removeResponder(Long sharedThoughtId, Long responderId) {
        SharedThought sharedThought = get(sharedThoughtId, false, true);
        User responder = userDao.get(responderId);

        if (sharedThought != null && responder != null) {

            // audit
            SharedThoughtAudit audit = new SharedThoughtAudit();
            audit.setSharedThought(sharedThought);
            audit.setDate(new Date());
            audit.setAction(SharedThoughtAuditAction.REMOVE_RESPONDER);
            audit.setResponder(responder);
            audit.setUser(securityUserManager.getLoggedInUser());
            sharedThoughtAuditDao.save(audit);

            return sharedThoughtDao.removeResponder(sharedThought, responder);
        } else {
            return false;
        }
    }

    @Override
    public boolean addMessage(Long sharedThoughtId, String message) {
        SharedThought sharedThought = get(sharedThoughtId, false, true);
        User loggedInUser = securityUserManager.getLoggedInUser();

        if (sharedThought != null) {

            Message savedMessage = sharedThoughtDao.createSharedThoughtMessage(sharedThought, message, loggedInUser);

            if (savedMessage != null) {
                // audit
                SharedThoughtAudit audit = new SharedThoughtAudit();
                audit.setSharedThought(sharedThought);
                audit.setDate(new Date());
                audit.setAction(SharedThoughtAuditAction.ADD_MESSAGE);
                audit.setMessage(savedMessage);
                audit.setUser(loggedInUser);
                sharedThoughtAuditDao.save(audit);

                setUnviewed(sharedThought);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean checkAccessSharingThoughts(User user) {
        return sharedThoughtDao.checkAccessSharingThoughts(user);
    }
}
