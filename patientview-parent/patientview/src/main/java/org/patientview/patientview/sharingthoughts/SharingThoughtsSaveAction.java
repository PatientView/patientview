package org.patientview.patientview.sharingthoughts;

import org.patientview.ibd.action.BaseAction;
import org.patientview.patientview.model.SharedThought;
import org.patientview.model.Unit;
import org.patientview.patientview.model.User;
import org.patientview.patientview.user.UserUtils;
import org.patientview.utils.LegacySpringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SharingThoughtsSaveAction extends BaseAction {

    private List<String> errors = new ArrayList<String>();

    public ActionForward execute(
            ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        errors = new ArrayList<String>();
        DynaActionForm dynaForm = (DynaActionForm) form;
        String forwardMapping = "";
        User user = UserUtils.retrieveUser(request);
        int positiveNegative = (Integer) dynaForm.get(SharingThoughts.POSITIVE_NEGATIVE);
        boolean isSubmitted = (null != request.getParameter(SharingThoughts.SUBMIT));

        if (isSubmitted) {
            if (positiveNegative == 1) {
                if (validatePositiveThought(dynaForm)) {
                    forwardMapping = "submit";
                } else {
                    request.setAttribute(SharingThoughts.ERRORS_PARAM, errors);
                    return mapping.findForward("input_positive");
                }
            } else {
                if (validateNegativeThought(dynaForm)) {
                    forwardMapping = "submit";
                } else {
                    request.setAttribute(SharingThoughts.ERRORS_PARAM, errors);
                    return mapping.findForward("input_negative");
                }
            }
        } else {
            forwardMapping = "savedraft";
        }

        Long thoughtId = null;
        try {
            thoughtId = (Long) dynaForm.get(SharingThoughts.ID);
        } catch (Exception ignored) { ignored = null; }

        Boolean isPatient = null;
        try {
            isPatient = (Boolean) dynaForm.get(SharingThoughts.IS_PATIENT);
        } catch (Exception ignored) { ignored = null; }

        Boolean isPrincipalCarer = null;
        try {
            isPrincipalCarer = (Boolean) dynaForm.get(SharingThoughts.IS_PRINCIPAL_CARER);
        } catch (Exception ignored) { ignored = null; }

        Boolean isRelative = null;
        try {
            isRelative = (Boolean) dynaForm.get(SharingThoughts.IS_RELATIVE);
        } catch (Exception ignored) { ignored = null; }

        Boolean isFriend = null;
        try {
            isFriend = (Boolean) dynaForm.get(SharingThoughts.IS_FRIEND);
        } catch (Exception ignored) { ignored = null; }

        Boolean isAboutMe = null;
        try {
            isAboutMe = (Boolean) dynaForm.get(SharingThoughts.IS_ABOUT_ME);
        } catch (Exception ignored) { ignored = null; }

        Boolean isAboutOther = null;
        try {
            isAboutOther = (Boolean) dynaForm.get(SharingThoughts.IS_ABOUT_OTHER);
        } catch (Exception ignored) { ignored = null; }

        Boolean isAnonymous = null;
        try {
            isAnonymous = (Boolean) dynaForm.get(SharingThoughts.IS_ANONYMOUS);
        } catch (Exception ignored) { ignored = null; }
        Date startDate = null;
        try {
            startDate = convertFormDateString(SharingThoughts.START_DATE, dynaForm);
        } catch (Exception ignored) { ignored = null; }
        Date endDate = null;
        try {
            endDate = convertFormDateString(SharingThoughts.END_DATE, dynaForm);
        } catch (Exception ignored) { ignored = null; }
        Boolean isOngoing = null;
        try {
            isOngoing = (Boolean) dynaForm.get(SharingThoughts.IS_ONGOING);
        } catch (Exception ignored) { ignored = null; }
        String location = null;
        try {
            location = (String) dynaForm.get(SharingThoughts.LOCATION);
        } catch (Exception ignored) { ignored = null; }
        Unit unit = null;
        try {
            Long unitId = (Long) dynaForm.get(SharingThoughts.UNIT_ID);
            unit = LegacySpringUtils.getUnitManager().get(unitId);
        } catch (Exception ignored) { ignored = null; }
        String description = null;
        try {
            description = (String) dynaForm.get(SharingThoughts.DESCRIPTION);
        } catch (Exception ignored) { ignored = null; }
        String suggestedAction = null;
        try {
            suggestedAction = (String) dynaForm.get(SharingThoughts.SUGGESTED_ACTION);
        } catch (Exception ignored) { ignored = null; }
        String concernReason = null;
        try {
            concernReason = (String) dynaForm.get(SharingThoughts.CONCERN_REASON);
        } catch (Exception ignored) { ignored = null; }
        int likelihoodOfRecurrence = 0;
        try {
            likelihoodOfRecurrence = (Integer) dynaForm.get(SharingThoughts.LIKELIHOOD_0F_RECURRENCE);
        } catch (Exception ignored) { ignored = null; }
        int howSerious = 0;
        try {
            howSerious = (Integer) dynaForm.get(SharingThoughts.HOW_SERIOUS);
        } catch (Exception ignored) { ignored = null; }

        SharedThought thought = new SharedThought();
        thought.setUser(user);
        thought.setUnit(unit);
        thought.setPositiveNegative(positiveNegative);
        thought.setPatient(isPatient);
        thought.setPrincipalCarer(isPrincipalCarer);
        thought.setRelative(isRelative);
        thought.setFriend(isFriend);
        thought.setAboutMe(isAboutMe);
        thought.setAboutOther(isAboutOther);
        thought.setAnonymous(isAnonymous);
        thought.setStartDate(startDate);
        thought.setEndDate(endDate);
        thought.setOngoing(isOngoing);
        thought.setLocation(location);
        thought.setSuggestedAction(suggestedAction);
        thought.setDescription(description);
        thought.setConcernReason(concernReason);
        thought.setLikelihoodOfRecurrence(likelihoodOfRecurrence);
        thought.setHowSerious(howSerious);
        thought.setSubmitted(isSubmitted);

        if (thoughtId != null) {
            thought.setId(thoughtId);
        }

        getSharedThoughtManager().save(thought);

        if (!isSubmitted) {
            SharingThoughts.putThoughtListInRequest(request, user, true);
            SharingThoughts.putThoughtListInRequest(request, user, false);
        }

        return mapping.findForward(forwardMapping);
    }

    private boolean validatePositiveThought(DynaActionForm form) {
        boolean isValid = true;

        isValid = validatePositiveOrNegativeThought(form);

        return isValid;
    }

    private boolean validateNegativeThought(DynaActionForm form) {
        boolean isValid = true;

        isValid = validatePositiveOrNegativeThought(form);

        if ((null == form.get(SharingThoughts.CONCERN_REASON)) || "".equals(form.get(SharingThoughts.CONCERN_REASON)
        )) {
            errors.add("Please explain the reason");
            isValid = false;
        }

        if (null == form.get(SharingThoughts.LIKELIHOOD_0F_RECURRENCE)) {
            errors.add("Please show how likely this is to happen again");
            isValid = false;
        }

        if (null == form.get(SharingThoughts.HOW_SERIOUS)) {
            errors.add("Please tell us how serious this is");
            isValid = false;
        }

        return isValid;
    }

    private boolean validatePositiveOrNegativeThought(DynaActionForm form) {
        boolean isValid = true;

        if (null == form.get(SharingThoughts.IS_PATIENT)) {
            errors.add("Please tell us if you are the patient");
            isValid = false;
        }

        if (null != form.get(SharingThoughts.IS_PATIENT) && !(Boolean) form.get(SharingThoughts.IS_PATIENT)
                && null == form.get(SharingThoughts.IS_PRINCIPAL_CARER) && null == form.get(SharingThoughts.IS_RELATIVE)
                && null == form.get(SharingThoughts.IS_FRIEND)) {
            errors.add("If you are not the patient, please indicate who you are");
            isValid = false;
        }

        if (null == form.get(SharingThoughts.IS_ABOUT_ME) && null == form.get(SharingThoughts.IS_ABOUT_OTHER)) {
            errors.add("Please indicate who this is about");
            isValid = false;
        }

        if (null == form.get(SharingThoughts.IS_ANONYMOUS)) {
            errors.add("Please show whether you wish to remain anonymous");
            isValid = false;
        }

        if (null == form.get(SharingThoughts.START_DATE)
                || "".equals(form.get(SharingThoughts.START_DATE))
                || null == convertFormDateString(SharingThoughts.START_DATE, form)) {
            errors.add("Please enter a valid start date");
            isValid = false;
        }

        if (null != form.get(SharingThoughts.END_DATE) && !"".equals(form.get(SharingThoughts.END_DATE))
                && null == convertFormDateString(SharingThoughts.END_DATE, form)) {
            errors.add("Please enter a valid end date");
            isValid = false;
        }

        if (null == form.get(SharingThoughts.IS_ONGOING)) {
            errors.add("Please show whether this is still going on");
            isValid = false;
        }

        if ((null == form.get(SharingThoughts.LOCATION)) || "".equals(form.get(SharingThoughts.LOCATION))) {
            errors.add("Please enter a location");
            isValid = false;
        }

        if (null == form.get(SharingThoughts.DESCRIPTION) || "".equals(form.get(SharingThoughts.DESCRIPTION))) {
            errors.add("Please tell us what was good");
            isValid = false;
        }

        if ((null == form.get(SharingThoughts.SUGGESTED_ACTION)) || "".equals(form.get(SharingThoughts
                .SUGGESTED_ACTION))) {
            errors.add("Please tell us what can be done");
            isValid = false;
        }

        return isValid;
    }
}
