package com.solidstategroup.radar.service;

import com.solidstategroup.radar.model.exception.EmailAddressNotFoundException;
import com.solidstategroup.radar.model.exception.ProfessionalUserEmailAlreadyExists;
import com.solidstategroup.radar.model.exception.RegistrationException;
import com.solidstategroup.radar.model.user.PatientUser;
import com.solidstategroup.radar.model.user.ProfessionalUser;
import com.solidstategroup.radar.model.filter.ProfessionalUserFilter;
import com.solidstategroup.radar.model.filter.PatientUserFilter;

import java.util.Date;
import java.util.List;

public interface UserManager {

    PatientUser getPatientUser(String email);

    PatientUser getPatientUser(String email, Date dateOfBirth);

    List<PatientUser> getPatientUsers();

    List<PatientUser> getPatientUsers(PatientUserFilter filter);

    List<PatientUser> getPatientUsers(PatientUserFilter filter, int page, int numberPerPage);

    void savePatientUser(PatientUser patientUser);

    void registerPatient(PatientUser patientUser) throws RegistrationException;

    void registerProfessional(ProfessionalUser professionalUser) throws ProfessionalUserEmailAlreadyExists,
            RegistrationException;

    ProfessionalUser getProfessionalUser(Long id);

    ProfessionalUser getProfessionalUser(String email);

    void saveProfessionalUser(ProfessionalUser professionalUser) throws Exception;

    void sendForgottenPasswordToPatient(String username) throws EmailAddressNotFoundException;

    void sendForgottenPasswordToProfessional(String username) throws EmailAddressNotFoundException;

    void deleteProfessionalUser(ProfessionalUser professionalUser) throws Exception;

    List<ProfessionalUser> getProfessionalUsers();

    List<ProfessionalUser> getProfessionalUsers(ProfessionalUserFilter filter);

    List<ProfessionalUser> getProfessionalUsers(ProfessionalUserFilter filter, int page, int numberPerPage);

}
