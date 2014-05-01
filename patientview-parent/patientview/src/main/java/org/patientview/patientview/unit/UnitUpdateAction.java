/*
 * PatientView
 *
 * Copyright (c) Worth Solutions Limited 2004-2013
 *
 * This file is part of PatientView.
 *
 * PatientView is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * PatientView is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with PatientView in a file
 * titled COPYING. If not, see <http://www.gnu.org/licenses/>.
 *
 * @package PatientView
 * @link http://www.patientview.org
 * @author PatientView <info@patientview.org>
 * @copyright Copyright (c) 2004-2013, Worth Solutions Limited
 * @license http://www.gnu.org/licenses/gpl-3.0.html The GNU General Public License V3.0
 */

package org.patientview.patientview.unit;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.patientview.model.Unit;
import org.patientview.patientview.edtacode.EdtaCodeUtils;
import org.patientview.patientview.logon.LogonUtils;
import org.patientview.patientview.model.EdtaCode;
import org.patientview.service.EdtaCodeManager;
import org.patientview.service.UnitManager;
import org.patientview.service.UserManager;
import org.springframework.web.struts.ActionSupport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UnitUpdateAction extends ActionSupport {

    private UnitManager unitManager;
    private UserManager userManager;
    private EdtaCodeManager edtaCodeManager;

    public ActionForward execute(
        ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // get managers
        unitManager = getWebApplicationContext().getBean(UnitManager.class);
        userManager = getWebApplicationContext().getBean(UserManager.class);
        edtaCodeManager = getWebApplicationContext().getBean(EdtaCodeManager.class);

        // save unit
        Unit unit = unitManager.get(BeanUtils.getProperty(form, "unitcode"));
        UnitUtils.buildUnit(unit, form);
        unitManager.save(unit);

        // check if unit is a Radar Disease Group
        boolean isRadarGroup = "radargroup".equalsIgnoreCase(unit.getSourceType());

        // if Radar unit (Disease Group) then save EdtaCode containing info links for that Radar unit
        if (isRadarGroup) {
            EdtaCode edtaCode = edtaCodeManager.getEdtaCode(unit.getUnitcode());
            if (edtaCode == null) {
                edtaCode = new EdtaCode(unit.getUnitcode());
                edtaCode.setLinkType("unitLinks");
            }
            EdtaCodeUtils.build(request, edtaCode);
            edtaCodeManager.save(edtaCode);
            request.setAttribute("isRadarGroup", isRadarGroup);
        }

        if (userManager.getCurrentSpecialtyRole(userManager.getLoggedInUser()).equals("superadmin")) {
            request.setAttribute("units", unitManager.getAdminsUnits(isRadarGroup));
        } else {
            request.setAttribute("units", unitManager.getLoggedInUsersUnits());
        }

        return LogonUtils.logonChecks(mapping, request);
    }
}
