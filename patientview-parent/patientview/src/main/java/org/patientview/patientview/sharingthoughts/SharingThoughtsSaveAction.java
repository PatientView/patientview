package org.patientview.patientview.sharingthoughts;

import org.patientview.ibd.action.BaseAction;
import org.patientview.patientview.logging.AddLog;
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
        Long thoughtId = (dynaForm.get(SharingThoughts.ID) == null) ? null : (Long) dynaForm.get(SharingThoughts.ID);

        SharedThought thought = new SharedThought();
        thought.setUser(user);
        thought.setPositiveNegative(positiveNegative);
        thought.setSubmitted(isSubmitted);

        // ID
        thought.setId(thoughtId);

        // radio buttons Yes/No, null if not set
        thought.setPatient((dynaForm.get(SharingThoughts.IS_PATIENT) == null) ? null
                : (Boolean) dynaForm.get(SharingThoughts.IS_PATIENT));
        thought.setAnonymous((dynaForm.get(SharingThoughts.IS_ANONYMOUS) == null) ? null
                : (Boolean) dynaForm.get(SharingThoughts.IS_ANONYMOUS));
        thought.setOngoing((dynaForm.get(SharingThoughts.IS_ONGOING) == null) ? null
                : (Boolean) dynaForm.get(SharingThoughts.IS_ONGOING));

        // radio button ratings, null if not set
        thought.setLikelihoodOfRecurrence((dynaForm.get(SharingThoughts.LIKELIHOOD_0F_RECURRENCE) == null) ? null
                : (Integer) dynaForm.get(SharingThoughts.LIKELIHOOD_0F_RECURRENCE));
        thought.setHowSerious((dynaForm.get(SharingThoughts.HOW_SERIOUS) == null) ? null
                : (Integer) dynaForm.get(SharingThoughts.HOW_SERIOUS));

        // checkboxes, true if checked, otherwise false
        thought.setPrincipalCarer((dynaForm.get(SharingThoughts.IS_PRINCIPAL_CARER) == null) ? false : true);
        thought.setRelative((dynaForm.get(SharingThoughts.IS_RELATIVE) == null) ? false : true);
        thought.setFriend((dynaForm.get(SharingThoughts.IS_FRIEND) == null) ? false : true);
        thought.setAboutMe((dynaForm.get(SharingThoughts.IS_ABOUT_ME) == null) ? false : true);
        thought.setAboutOther((dynaForm.get(SharingThoughts.IS_ABOUT_OTHER) == null) ? false : true);

        // dates, null if blank or error converting
        Date startDate = null;
        try {
            startDate = convertFormDateString(SharingThoughts.START_DATE, dynaForm);
        } catch (Exception ignored) { ignored = null; }
        thought.setStartDate(startDate);

        Date endDate = null;
        try {
            endDate = convertFormDateString(SharingThoughts.END_DATE, dynaForm);
        } catch (Exception ignored) { ignored = null; }
        thought.setEndDate(endDate);

        // text fields
        thought.setLocation((dynaForm.get(SharingThoughts.LOCATION) == null) ? null
                : (String) dynaForm.get(SharingThoughts.LOCATION));
        thought.setDescription((dynaForm.get(SharingThoughts.DESCRIPTION) == null) ? null
                : (String) dynaForm.get(SharingThoughts.DESCRIPTION));
        thought.setSuggestedAction((dynaForm.get(SharingThoughts.SUGGESTED_ACTION) == null) ? null
                : (String) dynaForm.get(SharingThoughts.SUGGESTED_ACTION));
        thought.setConcernReason((dynaForm.get(SharingThoughts.CONCERN_REASON) == null) ? null
                : (String) dynaForm.get(SharingThoughts.CONCERN_REASON));

        // dropdown for unit
        Unit unit = null;
        try {
            Long unitId = (Long) dynaForm.get(SharingThoughts.UNIT_ID);
            unit = LegacySpringUtils.getUnitManager().get(unitId);
        } catch (Exception ignored) { ignored = null; }
        thought.setUnit(unit);

        if (isSubmitted) {
            if (positiveNegative == 1) {
                if (validatePositiveThought(dynaForm)) {
                    forwardMapping = "submit";
                } else {
                    request.setAttribute(SharingThoughts.ERRORS_PARAM, errors);
                    if (thoughtId == null) {
                        return mapping.findForward("input_positive");
                    } else {
                        request.setAttribute(SharingThoughts.THOUGHT_PARAM, thought);
                        return mapping.findForward("input_edit_positive");
                    }
                }
            } else {
                if (validateNegativeThought(dynaForm)) {
                    forwardMapping = "submit";
                } else {
                    request.setAttribute(SharingThoughts.ERRORS_PARAM, errors);
                    if (thoughtId == null) {
                        return mapping.findForward("input_negative");
                    } else {
                        request.setAttribute(SharingThoughts.THOUGHT_PARAM, thought);
                        return mapping.findForward("input_edit_negative");
                    }
                }
            }
        } else {
            SharingThoughts.putThoughtListInRequest(request, user, true);
            SharingThoughts.putThoughtListInRequest(request, user, false);
            forwardMapping = "savedraft";
        }

        getSharedThoughtManager().save(thought, isSubmitted);

        return mapping.findForward(forwardMapping);
    }

    private boolean validatePositiveThought(DynaActionForm form) {
        return validatePositiveOrNegativeThought(form);
    }

    private boolean validateNegativeThought(DynaActionForm form) {
        boolean isValid = validatePositiveOrNegativeThought(form);

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
