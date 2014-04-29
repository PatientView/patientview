package org.patientview.patientview.sharingthoughts;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.patientview.ibd.action.BaseAction;
import org.patientview.patientview.logging.AddLog;
import org.patientview.patientview.model.SharedThought;
import org.patientview.patientview.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SharingThoughtsAdminViewThoughtAction extends BaseAction {

    public ActionForward execute(
            ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm dynaForm = (DynaActionForm) form;
        long thoughtId = (Long) dynaForm.get(SharingThoughts.ID);
        SharedThought thought = getSharedThoughtManager().get(thoughtId);
        User loggedInUser = getSecurityUserManager().getLoggedInUser();

        request.setAttribute(SharingThoughts.THOUGHT_PARAM, thought);
        request.setAttribute("user", loggedInUser);

        // log viewing, store SharedThought.id in log.extrainfo
        AddLog.addLog(loggedInUser.getUsername(), AddLog.SHARED_THOUGHT_VIEW, loggedInUser.getUsername(), "",
                thought.getUnit().getUnitcode(), thought.getId().toString());

        return mapping.findForward("success");
    }
}
