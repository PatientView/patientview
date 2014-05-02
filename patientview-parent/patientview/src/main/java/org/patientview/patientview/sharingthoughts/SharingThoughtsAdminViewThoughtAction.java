package org.patientview.patientview.sharingthoughts;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.patientview.ibd.action.BaseAction;
import org.patientview.patientview.messaging.Messaging;
import org.patientview.patientview.model.SharedThought;
import org.patientview.patientview.model.User;
import org.patientview.service.SharedThoughtManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SharingThoughtsAdminViewThoughtAction extends BaseAction {

    private SharedThoughtManager sharedThoughtManager;

    public ActionForward execute(
            ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        sharedThoughtManager = getWebApplicationContext().getBean(SharedThoughtManager.class);

        DynaActionForm dynaForm = (DynaActionForm) form;
        long thoughtId = (Long) dynaForm.get(SharingThoughts.ID);
        SharedThought thought = getSharedThoughtManager().get(thoughtId, true, true);
        User loggedInUser = getSecurityUserManager().getLoggedInUser();

        request.setAttribute(SharingThoughts.THOUGHT_PARAM, thought);
        request.setAttribute("user", loggedInUser);

        sharedThoughtManager.setViewed(thought, loggedInUser);
        request.getSession().setAttribute(Messaging.SHARING_THOUGHTS_UNVIEWED_COUNT
                , sharedThoughtManager.getStaffThoughtList(loggedInUser, true).size());

        return mapping.findForward("success");
    }
}