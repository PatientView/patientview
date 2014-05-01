package org.patientview.repository;

import org.patientview.patientview.model.Message;
import org.patientview.patientview.model.SharedThought;
import org.patientview.patientview.model.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(propagation = Propagation.MANDATORY)
public interface SharedThoughtDao {

    List<SharedThought> getAll(boolean orderBySubmitDate);

    SharedThought get(Long id);

    List<SharedThought> getUsersThoughts(User user, boolean isSubmitted);

    List<SharedThought> getStaffThoughtList(User user);

    void save(SharedThought thought);

    void delete(Long id);

    List<User> getOtherResponders(SharedThought sharedThought);

    boolean addResponder(SharedThought sharedThought, User responder);

    boolean removeResponder(SharedThought sharedThought, User responder);

    Message createSharedThoughtMessage(SharedThought sharedThought, String content, User sender);

    boolean checkAccessSharingThoughts(User user);
}
