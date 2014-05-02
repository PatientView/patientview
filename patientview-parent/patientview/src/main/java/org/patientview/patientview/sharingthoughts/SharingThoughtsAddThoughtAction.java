package org.patientview.patientview.sharingthoughts;

import org.patientview.ibd.action.BaseAction;
import org.patientview.patientview.unit.UnitUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SharingThoughtsAddThoughtAction extends BaseAction {

    public ActionForward execute(
            ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        UnitUtils.putRelevantUnitsInRequest(request);

        return mapping.findForward("success");
    }
}
