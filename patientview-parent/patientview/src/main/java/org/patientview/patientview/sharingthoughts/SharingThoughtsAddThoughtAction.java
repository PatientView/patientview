package org.patientview.patientview.sharingthoughts;

import org.patientview.ibd.action.BaseAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.patientview.service.UnitManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SharingThoughtsAddThoughtAction extends BaseAction {

    UnitManager unitManager;

    public ActionForward execute(
            ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        unitManager = getWebApplicationContext().getBean(UnitManager.class);
        request.getSession().setAttribute("units", unitManager.getLoggedInUsersRenalUnits());
        return mapping.findForward("success");
    }
}
