package com.worthsoln.patientview.splashpage;

import com.worthsoln.HibernateUtil;
import com.worthsoln.patientview.logon.LogonUtils;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SplashPageEditAction extends Action {

    public ActionForward execute(
            ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String id = BeanUtils.getProperty(form, "id");
        Integer idInt = Integer.decode(id);

        Session session = HibernateUtil.currentSession();
        Transaction tx = session.beginTransaction();
        SplashPage splashpage = (SplashPage) session.get(SplashPage.class, idInt);
        tx.commit();
        HibernateUtil.closeSession();

        request.setAttribute("splashPage", splashpage);

        SplashPageUtils.putSplashPageUnitsInRequest(request);

        return LogonUtils.logonChecks(mapping, request);
    }
}