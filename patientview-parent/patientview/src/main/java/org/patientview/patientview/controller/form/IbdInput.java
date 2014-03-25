package org.patientview.patientview.controller.form;

/**
 * Created by james@solidstategroup.com on 21/03/2014.
 */
public class IbdInput {

    private String primaryDiagnosis;
    private String diseaseExtent;
    private Integer yearOfDiagnosis;
    private String complications;
    private String partsAffected;
    private String familyHistory;
    private String surgicalHistory;
    private String vaccinationRecord;
    private String yearOfColonoscopy;
    private Integer consultant;

    public String getPrimaryDiagnosis() {
        return primaryDiagnosis;
    }

    public void setPrimaryDiagnosis(final String primaryDiagnosis) {
        this.primaryDiagnosis = primaryDiagnosis;
    }

    public String getDiseaseExtent() {
        return diseaseExtent;
    }

    public void setDiseaseExtent(final String diseaseExtent) {
        this.diseaseExtent = diseaseExtent;
    }

    public Integer getYearOfDiagnosis() {
        return yearOfDiagnosis;
    }

    public void setYearOfDiagnosis(final Integer yearOfDiagnosis) {
        this.yearOfDiagnosis = yearOfDiagnosis;
    }

    public String getComplications() {
        return complications;
    }

    public void setComplications(final String complications) {
        this.complications = complications;
    }

    public String getPartsAffected() {
        return partsAffected;
    }

    public void setPartsAffected(final String partsAffected) {
        this.partsAffected = partsAffected;
    }

    public String getFamilyHistory() {
        return familyHistory;
    }

    public void setFamilyHistory(final String familyHistory) {
        this.familyHistory = familyHistory;
    }

    public String getSurgicalHistory() {
        return surgicalHistory;
    }

    public void setSurgicalHistory(final String surgicalHistory) {
        this.surgicalHistory = surgicalHistory;
    }

    public String getVaccinationRecord() {
        return vaccinationRecord;
    }

    public void setVaccinationRecord(final String vaccinationRecord) {
        this.vaccinationRecord = vaccinationRecord;
    }

    public String getYearOfColonoscopy() {
        return yearOfColonoscopy;
    }

    public void setYearOfColonoscopy(final String yearOfColonoscopy) {
        this.yearOfColonoscopy = yearOfColonoscopy;
    }

    public Integer getConsultant() {
        return consultant;
    }

    public void setConsultant(final Integer consultant) {
        this.consultant = consultant;
    }
}
