package org.patientview.patientview.sharingthoughts;

import org.patientview.ibd.action.BaseAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.patientview.service.SecurityUserManager;
import org.patientview.service.SharedThoughtManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SharingThoughtsHomeAction extends BaseAction {

    private SharedThoughtManager sharedThoughtManager;
    private SecurityUserManager securityUserManager;

    public ActionForward execute(
            ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // check if user has access to Sharing Thoughts
        sharedThoughtManager = getWebApplicationContext().getBean(SharedThoughtManager.class);
        securityUserManager = getWebApplicationContext().getBean(SecurityUserManager.class);

        if (sharedThoughtManager.checkAccessSharingThoughts(securityUserManager.getLoggedInUser())) {
            return mapping.findForward("success");
        } else {
            return mapping.findForward("noPermission");
        }
    }
}
