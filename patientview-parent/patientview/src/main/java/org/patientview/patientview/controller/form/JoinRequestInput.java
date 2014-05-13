package org.patientview.patientview.controller.form;

/**
 * Created by james@solidtstategroup.com on 12/05/2014.
 */
public class JoinRequestInput {

    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String nhsNumber;
    private String unitId;
    private String email;
    private String securityQuestion;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(final String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNhsNumber() {
        return nhsNumber;
    }

    public void setNhsNumber(final String nhsNumber) {
        this.nhsNumber = nhsNumber;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(final String unitId) {
        this.unitId = unitId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(final String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }
}
