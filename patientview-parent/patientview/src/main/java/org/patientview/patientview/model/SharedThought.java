/*
 * PatientView
 *
 * Copyright (c) Worth Solutions Limited 2004-2013
 *
 * This file is part of PatientView.
 *
 * PatientView is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * PatientView is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with PatientView in a file
 * titled COPYING. If not, see <http://www.gnu.org/licenses/>.
 *
 * @package PatientView
 * @link http://www.patientview.org
 * @author PatientView <info@patientview.org>
 * @copyright Copyright (c) 2004-2013, Worth Solutions Limited
 * @license http://www.gnu.org/licenses/gpl-3.0.html The GNU General Public License V3.0
 */

package org.patientview.patientview.model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.patientview.model.BaseModel;
import org.patientview.model.Unit;
import org.patientview.ibd.Ibd;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class SharedThought extends BaseModel {

    public static final int SHORT_DESCRIPTION_LENGTH = 49;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @OneToOne(optional = true)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @Column(name = "positive_negative", nullable = false)
    private int positiveNegative;

    @Column(name = "is_patient")
    private Boolean isPatient = false;

    @Column(name = "is_principal_carer")
    private Boolean isPrincipalCarer = false;

    @Column(name = "is_relative")
    private Boolean isRelative = false;

    @Column(name = "is_friend")
    private Boolean isFriend = false;

    @Column(name = "is_about_me")
    private Boolean isAboutMe = false;

    @Column(name = "is_about_other")
    private Boolean isAboutOther = false;

    @Column(name = "is_anonymous")
    private Boolean isAnonymous = false;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "is_ongoing")
    private Boolean isOngoing = false;

    @Column(name = "location")
    private String location;

    @Column(name = "description")
    private String description;

    @Column(name = "suggested_action")
    private String suggestedAction;

    @Column(name = "concern_reason")
    private String concernReason;

    @Column(name = "likelihood_recurrence")
    private Integer likelihoodOfRecurrence;

    @Column(name = "how_serious")
    private Integer howSerious;

    @Column(name = "is_submitted")
    private boolean isSubmitted = false;

    // this will be set by manager
    @Column(name = "date_last_saved")
    private Date dateLastSaved;

    @ManyToOne
    @JoinColumn(name = "allocation_user_id")
    private User allocationUser;

    @Column(name = "is_action_taken")
    private Boolean isActionTaken = false;

    @ManyToOne
    @JoinColumn(name = "filterer_user_id")
    private User filtererUser;

    @Column(name = "is_patient_contacted")
    private Boolean isPatientContacted = false;

    @Column(name = "notes")
    private String notes;

    @Column(name = "status")
    private String status;

    @Column(name = "is_viewed")
    private Boolean isViewed = false;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(
            name = "user_sharedthought",
            joinColumns = { @JoinColumn(name = "sharedthought_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private List<User> responders;

    public SharedThought() {
        responders = new ArrayList<User>();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public int getPositiveNegative() {
        return positiveNegative;
    }

    public void setPositiveNegative(int positiveNegative) {
        this.positiveNegative = positiveNegative;
    }

    public Boolean getPatient() {
        return isPatient;
    }

    public void setPatient(Boolean patient) {
        isPatient = patient;
    }

    public Boolean getPrincipalCarer() {
        return isPrincipalCarer;
    }

    public void setPrincipalCarer(Boolean getPrincipalCarer) {
        this.isPrincipalCarer = isPrincipalCarer;
    }

    public Boolean getRelative() {
        return isRelative;
    }

    public void setRelative(Boolean getRelative) {
        this.isRelative = isRelative;
    }

    public Boolean getFriend() {
        return isFriend;
    }

    public void setFriend(Boolean getFriend) {
        this.isFriend = isFriend;
    }

    public Boolean getAboutMe() {
        return isAboutMe;
    }

    public void setAboutMe(Boolean aboutMe) {
        this.isAboutMe = aboutMe;
    }

    public Boolean getAboutOther() {
        return isAboutOther;
    }

    public void setAboutOther(Boolean aboutOther) {
        isAboutOther = aboutOther;
    }

    public Boolean getAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.isAnonymous = anonymous;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getOngoing() {
        return isOngoing;
    }

    public void setOngoing(Boolean ongoing) {
        isOngoing = ongoing;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSuggestedAction() {
        return suggestedAction;
    }

    public void setSuggestedAction(String suggestedAction) {
        this.suggestedAction = suggestedAction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConcernReason() {
        return concernReason;
    }

    public void setConcernReason(String concernReason) {
        this.concernReason = concernReason;
    }

    public Integer getLikelihoodOfRecurrence() {
        return likelihoodOfRecurrence;
    }

    public void setLikelihoodOfRecurrence(Integer likelihoodOfRecurrence) {
        this.likelihoodOfRecurrence = likelihoodOfRecurrence;
    }

    public Integer getHowSerious() {
        return howSerious;
    }

    public void setHowSerious(Integer howSerious) {
        this.howSerious = howSerious;
    }

    public Boolean isSubmitted() {
        return isSubmitted;
    }

    public void setSubmitted(boolean submitted) {
        isSubmitted = submitted;
    }

    public Date getDateLastSaved() {
        return dateLastSaved;
    }

    public void setDateLastSaved(Date dateLastSaved) {
        this.dateLastSaved = dateLastSaved;
    }

    public User getAllocationUser() {
        return allocationUser;
    }

    public void setAllocationUser(User allocationUser) {
        this.allocationUser = allocationUser;
    }

    public Boolean getActionTaken() {
        return isActionTaken;
    }

    public void setActionTaken(Boolean actionTaken) {
        isActionTaken = actionTaken;
    }

    public User getFiltererUser() {
        return filtererUser;
    }

    public void setFiltererUser(User filtererUser) {
        this.filtererUser = filtererUser;
    }

    public Boolean getPatientContacted() {
        return isPatientContacted;
    }

    public void setPatientContacted(Boolean patientContacted) {
        isPatientContacted = patientContacted;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getViewed() {
        return isViewed;
    }

    public void setViewed(Boolean viewed) {
        isViewed = viewed;
    }

    public String getDateLastSavedFormattedDate() {
        return getDateFormattedDate(dateLastSaved);
    }

    public String getDateLastSavedFormattedDateTime() {
        return getDateFormattedDateTime(dateLastSaved);
    }

    public String getStartDateFormattedDate() {
        return getDateFormattedDate(startDate);
    }

    public String getStartDateFormattedDateTime() {
        return getDateFormattedDateTime(startDate);
    }

    public String getEndDateFormattedDate() {
        return getDateFormattedDate(endDate);
    }

    public String getEndDateFormattedDateTime() {
        return getDateFormattedDateTime(endDate);
    }

    private String getDateFormattedDate(Date date) {
        if (date != null) {
            return Ibd.DATE_FORMAT.format(date);
        }

        return "";
    }

    private String getDateFormattedDateTime(Date date) {
        if (date != null) {
            return Ibd.DATE_TIME_FORMAT.format(date);
        }

        return "";
    }

    public String getDescriptionBeginning() {
        return (description.length() >= SHORT_DESCRIPTION_LENGTH) ? description.substring(0, SHORT_DESCRIPTION_LENGTH)
                : description;
    }

    public List<User> getResponders() {
        return responders;
    }


    public void setResponders(List<User> responders) {
        this.responders = responders;
    }
}
