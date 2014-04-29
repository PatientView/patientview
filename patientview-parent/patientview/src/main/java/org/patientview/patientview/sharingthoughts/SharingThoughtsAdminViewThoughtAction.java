package org.patientview.patientview.sharingthoughts;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.patientview.ibd.action.BaseAction;
import org.patientview.patientview.model.SharedThought;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SharingThoughtsAdminViewThoughtAction extends BaseAction {

    public ActionForward execute(
            ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm dynaForm = (DynaActionForm) form;
        long thoughtId = (Long) dynaForm.get(SharingThoughts.ID);
        SharedThought thought = getSharedThoughtManager().get(thoughtId);

        request.setAttribute(SharingThoughts.THOUGHT_PARAM, thought);
        request.setAttribute("user", getSecurityUserManager().getLoggedInUser());

        return mapping.findForward("success");
    }
}
