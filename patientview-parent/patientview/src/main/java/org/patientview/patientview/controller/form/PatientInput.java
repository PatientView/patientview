package org.patientview.patientview.controller.form;

/**
 * Created by eatek on 21/03/2014.
 */
public class PatientInput {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private boolean emailVerified ;
    private String nhsNo;
    private String unitCode;
    private String overrideDuplicateNhsNo;
    private boolean firstLogon;
    private boolean dummyPatient;
    private boolean accountLocked;
    private boolean accountHidden;


    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(final boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getNhsNo() {
        return nhsNo;
    }

    public void setNhsNo(final String nhsNo) {
        this.nhsNo = nhsNo;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(final String unitCode) {
        this.unitCode = unitCode;
    }

    public String getOverrideDuplicateNhsNo() {
        return overrideDuplicateNhsNo;
    }

    public void setOverrideDuplicateNhsNo(final String overrideDuplicateNhsNo) {
        this.overrideDuplicateNhsNo = overrideDuplicateNhsNo;
    }

    public boolean isFirstLogon() {
        return firstLogon;
    }

    public void setFirstLogon(final boolean firstLogon) {
        this.firstLogon = firstLogon;
    }

    public boolean isDummyPatient() {
        return dummyPatient;
    }

    public void setDummyPatient(final boolean dummyPatient) {
        this.dummyPatient = dummyPatient;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(final boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isAccountHidden() {
        return accountHidden;
    }

    public void setAccountHidden(final boolean accountHidden) {
        this.accountHidden = accountHidden;
    }
}
