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

package org.patientview.repository;

import org.patientview.model.Specialty;
import org.patientview.model.Unit;
import org.patientview.patientview.logon.UnitAdmin;
import org.patientview.patientview.model.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Transactional(propagation = Propagation.MANDATORY)
public interface UnitDao {

    Unit get(Long id);

    Unit get(String unitCode, Specialty specialty);

    void save(Unit unit);

    List<Unit> getAll(boolean sortByName);

    List<Unit> getAll(boolean sortByName, Specialty specialty);

    List<Unit> getAll(String[] sourceTypesToExclude, String[] sourceTypesToInclude);

    List<Unit> getUnitsWithUser(Specialty specialty);

    List<Unit> getAdminsUnits(Specialty specialty);

    List<Unit> getAdminsUnits(Specialty specialty, boolean isRadarGroup);

    List<Unit> get(List<String> usersUnitCodes, Specialty specialty);

    List<Unit> get(List<String> usersUnitCodes, String[] notTheseUnitCodes, String[] plusTheseUnitCodes,
                   Specialty specialty);

    List<UnitAdmin> getUnitUsers(String unitcode, Specialty specialty);

    List<UnitAdmin> getAllUnitUsers(Specialty specialty);

    List<User> getUnitPatientUsers(String unitcode, Specialty specialty);
}
