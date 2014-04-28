package org.patientview.service.impl;

import org.patientview.patientview.model.SharedThought;
import org.patientview.patientview.model.User;
import org.patientview.repository.SharedThoughtDao;
import org.patientview.repository.UserDao;
import org.patientview.service.SecurityUserManager;
import org.patientview.service.SharedThoughtManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.GregorianCalendar;
import java.util.List;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@Service(value = "sharedThoughtManager")
public class SharedThoughtManagerImpl implements SharedThoughtManager {

    @Inject
    private SharedThoughtDao sharedThoughtDao;
    @Inject
    private UserDao userDao;
    @Inject
    private SecurityUserManager securityUserManager;

    @Override
    public List<SharedThought> getAll() {
        return sharedThoughtDao.getAll();
    }

    @Override
    public SharedThought get(Long sharedThoughtId) {
        return sharedThoughtDao.get(sharedThoughtId);
    }

    @Override
    public List<SharedThought> getUsersThoughts(Long userId, boolean isSubmitted) {
        return sharedThoughtDao.getUsersThoughts(userId, isSubmitted);
    }

    @Override
    public void delete(Long sharedThoughtId) {
        sharedThoughtDao.delete(sharedThoughtId);
    }

    @Override
    public void save(SharedThought thought) {
        thought.setDateLastSaved(GregorianCalendar.getInstance().getTime());
        sharedThoughtDao.save(thought);
    }

    @Override
    public List<User> getOtherResponders(SharedThought sharedThought) {
        return sharedThoughtDao.getOtherResponders(sharedThought);
    }

    @Override
    public boolean addResponder(Long sharedThoughtId, Long responderId) {
        SharedThought sharedThought = get(sharedThoughtId);
        User responder = userDao.get(responderId);

        if (sharedThought != null && responder != null) {
            return sharedThoughtDao.addResponder(sharedThought, responder);
        } else {
            return false;
        }
    }

    @Override
    public boolean removeResponder(Long sharedThoughtId, Long responderId) {
        SharedThought sharedThought = get(sharedThoughtId);
        User responder = userDao.get(responderId);

        if (sharedThought != null && responder != null) {
            return sharedThoughtDao.removeResponder(sharedThought, responder);
        } else {
            return false;
        }
    }

    @Override
    public boolean addMessage(Long sharedThoughtId, String message) {
        SharedThought sharedThought = get(sharedThoughtId);

        if (sharedThought != null) {
            return sharedThoughtDao.createSharedThoughtMessage(
                    sharedThought, message, securityUserManager.getLoggedInUser());
        } else {
            return false;
        }
    }
}
