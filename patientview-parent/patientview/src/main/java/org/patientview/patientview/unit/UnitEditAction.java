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
import org.patientview.patientview.logon.LogonUtils;
import org.patientview.patientview.model.EdtaCode;
import org.patientview.service.EdtaCodeManager;
import org.patientview.service.UnitManager;
import org.springframework.web.struts.ActionSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UnitEditAction extends ActionSupport {

    private UnitManager unitManager;
    private EdtaCodeManager edtaCodeManager;

    public ActionForward execute(
        ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        unitManager = getWebApplicationContext().getBean(UnitManager.class);
        edtaCodeManager = getWebApplicationContext().getBean(EdtaCodeManager.class);

        String unitcode = BeanUtils.getProperty(form, "unitcode");
        Unit unit = unitManager.get(unitcode);
        request.getSession().setAttribute("unit", unit);

        // get EdtaCode which will contain the static links for this unit only for Radargroup type units
        if (unit.getSourceType().equals("Radargroup")) {
            EdtaCode edtaCode = edtaCodeManager.getEdtaCode(unitcode);
            if (edtaCode == null) {
                edtaCode = new EdtaCode(unit.getUnitcode());
                edtaCode.setLinkType("unitLinks");
            }
            request.setAttribute(EdtaCode.getIdentifier(), edtaCode);
        }

        return LogonUtils.logonChecks(mapping, request);
    }

}
