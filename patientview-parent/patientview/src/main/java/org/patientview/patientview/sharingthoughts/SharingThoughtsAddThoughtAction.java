package org.patientview.patientview.sharingthoughts;

import org.patientview.ibd.action.BaseAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.patientview.service.SharedThoughtManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class SharingThoughtsAddThoughtAction extends BaseAction {

    private SharedThoughtManager sharedThoughtManager;

    public ActionForward execute(
            ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        sharedThoughtManager = getWebApplicationContext().getBean(SharedThoughtManager.class);
        request.getSession().setAttribute("units", sharedThoughtManager.getLoggedInUsersUnits());
        request.setAttribute(SharingThoughts.ERRORS_PARAM_MAP, new HashMap<String, Boolean>());
        return mapping.findForward("success");
    }
}
