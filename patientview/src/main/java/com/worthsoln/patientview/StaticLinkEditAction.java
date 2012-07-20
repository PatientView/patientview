package com.worthsoln.patientview;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.worthsoln.HibernateUtil;
import com.worthsoln.patientview.model.EdtaCode;
import com.worthsoln.patientview.logon.LogonUtils;

public class StaticLinkEditAction extends Action {

    public ActionForward execute(
        ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HibernateUtil.retrievePersistentObjectAndAddToRequestWithIdParameter(request, EdtaCode.class, "static",
            EdtaCode.getIdentifier());

        return LogonUtils.logonChecks(mapping, request);
    }
}
