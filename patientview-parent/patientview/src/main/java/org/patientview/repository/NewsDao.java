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
import org.patientview.patientview.model.News;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Transactional(propagation = Propagation.MANDATORY)
public interface NewsDao {

    News get(Long id);

    /**
     * Get news item as Everyone (general public)
     * @param id News item ID
     * @param specialty Specialty
     * @return News item if permissions ok, otherwise null
     */
    News getSingleNewsAsEveryone(Long id, Specialty specialty);

    /**
     * Get news item as Unit Admin
     * @param id News item ID
     * @param unitCodes List of unit codes the user belongs to
     * @param specialty Specialty
     * @return News item if permissions ok, otherwise null
     */
    News getSingleNewsAsAdmin(Long id, List<String> unitCodes, Specialty specialty);

    /**
     * Get news item as Unit patient
     * @param id News item ID
     * @param unitCodes List of unit codes the user belongs to
     * @param specialty Specialty
     * @return News item if permissions ok, otherwise null
     */
    News getSingleNewsAsPatient(Long id, List<String> unitCodes, Specialty specialty);

    void save(News news);

    void delete(News news);

    List<News> getAll(Specialty specialty);

    List<News> getNewsForEveryone(Specialty specialty);

    List<News> getAdminNewsForUnitCodes(List<String> unitCodes, Specialty specialty);

    List<News> getAdminEditNewsForUnitCodes(List<String> unitCodes, Specialty specialty);

    List<News> getPatientNewsForUnitCodes(List<String> unitCodes, Specialty specialty);
}
