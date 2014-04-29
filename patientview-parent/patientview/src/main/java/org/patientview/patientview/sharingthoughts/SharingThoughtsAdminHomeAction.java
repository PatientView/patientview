package org.patientview.patientview.sharingthoughts;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.patientview.service.SecurityUserManager;
import org.patientview.service.SharedThoughtManager;
import org.springframework.web.struts.ActionSupport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SharingThoughtsAdminHomeAction extends ActionSupport {

    private SharedThoughtManager sharedThoughtManager;
    private SecurityUserManager securityUserManager;

    public ActionForward execute(
            ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        sharedThoughtManager = getWebApplicationContext().getBean(SharedThoughtManager.class);
        securityUserManager = getWebApplicationContext().getBean(SecurityUserManager.class);

        request.setAttribute(
                "sharedThoughts", sharedThoughtManager.getStaffThoughtList(securityUserManager.getLoggedInUser()));

        return mapping.findForward("success");
    }
}
