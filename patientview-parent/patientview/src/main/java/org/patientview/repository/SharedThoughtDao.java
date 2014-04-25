package org.patientview.repository;

import org.patientview.patientview.model.SharedThought;
import org.patientview.patientview.model.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(propagation = Propagation.MANDATORY)
public interface SharedThoughtDao {

    List<SharedThought> getAll();

    SharedThought get(Long id);

    List<SharedThought> getUsersThoughts(Long userId, boolean isSubmitted);

    void save(SharedThought thought);

    void delete(Long id);

    List<User> getOtherResponders(SharedThought sharedThought);

    boolean addResponder(SharedThought sharedThought, User responder);
}
