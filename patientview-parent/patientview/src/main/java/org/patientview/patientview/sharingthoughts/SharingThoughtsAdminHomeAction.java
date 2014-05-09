package org.patientview.patientview.sharingthoughts;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.patientview.patientview.model.User;
import org.patientview.service.SecurityUserManager;
import org.patientview.service.SharedThoughtManager;
import org.patientview.service.UserManager;
import org.springframework.web.struts.ActionSupport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SharingThoughtsAdminHomeAction extends ActionSupport {

    private SharedThoughtManager sharedThoughtManager;
    private SecurityUserManager securityUserManager;
    private UserManager userManager;

    public ActionForward execute(
            ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        sharedThoughtManager = getWebApplicationContext().getBean(SharedThoughtManager.class);
        securityUserManager = getWebApplicationContext().getBean(SecurityUserManager.class);
        userManager = getWebApplicationContext().getBean(UserManager.class);
        User loggedInUser = securityUserManager.getLoggedInUser();

        if (userManager.getCurrentSpecialtyRole(loggedInUser).equalsIgnoreCase("superadmin")) {
            request.setAttribute("sharedThoughts", sharedThoughtManager.getSubmitted(false));
        } else {
            request.setAttribute("sharedThoughts", sharedThoughtManager.getStaffThoughtList(loggedInUser, false));
        }

        request.setAttribute("user", loggedInUser);

        return mapping.findForward("success");
    }
}
