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

package org.patientview.patientview.medicine;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.patientview.actionutils.ActionUtils;
import org.patientview.model.Unit;
import org.patientview.patientview.logon.LogonUtils;
import org.patientview.patientview.model.Medicine;
import org.patientview.patientview.model.User;
import org.patientview.patientview.unit.UnitUtils;
import org.patientview.patientview.user.UserUtils;
import org.patientview.utils.LegacySpringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MedicineDisplayAction extends Action {

    private static String ecrUnitcode = "ECS";
    private static String medicationsNonEcr = "nonECR";
    private static String medicationsEcr = "ECR";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {
        User user = null;
        String param = mapping.getParameter();
        boolean isRadarGroup = false;
        if ("medication".equals(param)) {
            isRadarGroup = true;
            user = UserUtils.retrieveUser(request);
        } else  if ("controlMedication".equals(param)) {
            isRadarGroup = true;
            String username = (String) request.getSession().getAttribute("userBeingViewedUsername");
            user = LegacySpringUtils.getUserManager().get(username);
        } else {
            user = UserUtils.retrieveUser(request);
        }

        HashMap medicines = getMedicinesForPatient(user, request, isRadarGroup);
        //sortNullDatesOnMedicines(medicines);

        request.setAttribute("medicines", medicines.get(medicationsNonEcr));
        request.setAttribute("medicinesECR", medicines.get(medicationsEcr));
        request.setAttribute("user", user);
        // ECR enabled for that user's unit?
        request.setAttribute("ecrEnabled", LegacySpringUtils.getUserManager().getEcrEnabled(user));

        ActionUtils.setUpNavLink(mapping.getParameter(), request);
        return LogonUtils.logonChecks(mapping, request);
    }

    private HashMap<String, List<MedicineWithShortName>> getMedicinesForPatient(
            User user, HttpServletRequest request, boolean isRadarGroup) throws Exception {
        List<MedicineWithShortName> medicinesWithShortName = new ArrayList<MedicineWithShortName>();
        List<MedicineWithShortName> medicinesWithShortNameECR = new ArrayList<MedicineWithShortName>();

        HashMap<String, List<MedicineWithShortName>> output = new HashMap<String, List<MedicineWithShortName>>();

        if (user != null) {
            List<Medicine> medicines = LegacySpringUtils.getMedicineManager().getUserMedicines(user);

            for (Medicine med : medicines) {
                Unit unit = UnitUtils.retrieveUnit(med.getUnitcode());
                if (unit != null) {
                    if (!isRadarGroup && "radargroup".equalsIgnoreCase(unit.getSourceType())) { continue; }
                    if (unit.getUnitcode().equals(ecrUnitcode)) {
                        medicinesWithShortNameECR.add(new MedicineWithShortName(med, unit.getShortname()));
                    } else {
                        medicinesWithShortName.add(new MedicineWithShortName(med, unit.getShortname()));
                    }
                } else {
                    medicinesWithShortName.add(new MedicineWithShortName(med, "UNKNOWN UNIT:" + med.getUnitcode()));
                }
            }
        }

        output.put(medicationsNonEcr, medicinesWithShortName);
        output.put(medicationsEcr, medicinesWithShortNameECR);
        return output;
    }

    private List sortNullDatesOnMedicines(List medicines) {
        for (Object obj : medicines) {
            Medicine medicine = (Medicine) obj;

            // todo this probably won't work anymore
            Medicine tempMed = LegacySpringUtils.getMedicineManager().get(medicine.getId());
            medicine.setStartdate(tempMed.getStartdate());
        }

        return medicines;
    }

}
