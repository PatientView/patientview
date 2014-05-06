package org.patientview.patientview.sharingthoughts;

import org.patientview.ibd.action.BaseAction;
import org.patientview.patientview.model.SharedThought;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.patientview.service.UnitManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SharingThoughtsRetrieveThoughtAction extends BaseAction {

    UnitManager unitManager;

    public ActionForward execute(
            ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm dynaForm = (DynaActionForm) form;
        long thoughtId = (Long) dynaForm.get(SharingThoughts.ID);
        SharedThought thought = getSharedThoughtManager().get(thoughtId, true, false);
        request.setAttribute(SharingThoughts.THOUGHT_PARAM, thought);

        unitManager = getWebApplicationContext().getBean(UnitManager.class);
        request.getSession().setAttribute("units", unitManager.getLoggedInUsersRenalUnits());

        if (thought.getPositiveNegative() == 1) {
            return mapping.findForward("positive");
        } else {
            return mapping.findForward("negative");
        }
    }
}
