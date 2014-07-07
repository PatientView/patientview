package org.patientview.patientview.sharingthoughts;

import org.patientview.ibd.action.BaseAction;
import org.patientview.model.Unit;
import org.patientview.patientview.model.SharedThought;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.patientview.service.SharedThoughtManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

public class SharingThoughtsRetrieveThoughtAction extends BaseAction {

    private SharedThoughtManager sharedThoughtManager;

    public ActionForward execute(
            ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm dynaForm = (DynaActionForm) form;
        long thoughtId = (Long) dynaForm.get(SharingThoughts.ID);
        SharedThought thought = getSharedThoughtManager().get(thoughtId, true, false);
        request.setAttribute(SharingThoughts.THOUGHT_PARAM, thought);

        sharedThoughtManager = getWebApplicationContext().getBean(SharedThoughtManager.class);
        List<Unit> units = sharedThoughtManager.getLoggedInUsersUnits();
        request.getSession().setAttribute("units", units);
        if (units.size() > 1) {
            request.setAttribute(SharingThoughts.MULTIPLE_UNITS, true);
        }
        request.setAttribute(SharingThoughts.ERRORS_PARAM_MAP, new HashMap<String, Boolean>());

        if (thought.getPositiveNegative() == 1) {
            return mapping.findForward("positive");
        } else {
            return mapping.findForward("negative");
        }
    }
}
