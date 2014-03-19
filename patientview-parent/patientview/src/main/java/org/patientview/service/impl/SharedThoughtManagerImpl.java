package org.patientview.service.impl;

import org.patientview.patientview.model.SharedThought;
import org.patientview.repository.SharedThoughtDao;
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

    @Override
    public SharedThought getSharedThought(Long sharedThoughtId) {
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
}
