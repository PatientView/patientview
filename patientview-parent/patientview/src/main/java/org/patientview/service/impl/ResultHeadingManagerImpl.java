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

package org.patientview.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.patientview.model.Specialty;
import org.patientview.patientview.model.Panel;
import org.patientview.patientview.model.ResultHeading;
import org.patientview.patientview.model.SpecialtyResultHeading;
import org.patientview.repository.ResultHeadingDao;
import org.patientview.service.ResultHeadingManager;
import org.patientview.service.SecurityUserManager;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * For now this class is resolving the specialty for the specialty result heading. This mapping needs to be fixed.
 *
 */
@Service(value = "resultHeadingManager")
public class ResultHeadingManagerImpl implements ResultHeadingManager {

    @Inject
    private ResultHeadingDao resultHeadingDao;

    @Inject
    private SecurityUserManager securityUserManager;

    @Override
    public ResultHeading get(String headingcode, Specialty specialty) {
        return getSpecialtyResultHeading(resultHeadingDao.get(headingcode, specialty), specialty);
    }

    @Override
    public List<ResultHeading> get(Specialty specialty) {
        return getSpecialtyResultHeadings(resultHeadingDao.get(specialty), specialty);
    }

    @Override
    public List<ResultHeading> getAll(Specialty specialty) {
        return getSpecialtyResultHeadings(resultHeadingDao.getAll(specialty), specialty);
    }

    @Override
    public List<ResultHeading> get(int panel, Specialty specialty) {
        return resultHeadingDao.get(panel, specialty);
    }



    /**
     * The result heading with the context of the specialty becomes a specialty test heading and linked to the
     * result heading.
     *
     * @param resultHeading
     * @param specialty
     */
    @Override
    public void save(final ResultHeading resultHeading, final Specialty specialty) {

        if (!resultHeading.hasValidId()) {
            resultHeadingDao.save(resultHeading);
        }
        ResultHeading resultHeadingTemp = resultHeadingDao.get(resultHeading.getId());

        boolean foundSpecialty = false;
        for (SpecialtyResultHeading specialtyResultHeading : resultHeadingTemp.getSpecialtyResultHeadings()) {

            if (specialtyResultHeading.getSpecialtyId() == specialty.getId().intValue()
                    && specialtyResultHeading.getSpecialtyId() == specialty.getId().intValue()) {
                foundSpecialty = true;
                specialtyResultHeading.setRollover(resultHeading.getRollover());
                specialtyResultHeading.setHeading(resultHeading.getHeading());
                specialtyResultHeading.setPanel(resultHeading.getPanel());
                specialtyResultHeading.setPanelOrder(resultHeading.getPanelorder());

            }
        }

        if (!foundSpecialty) {

            SpecialtyResultHeading specialtyResultHeading = createSpecialtyResultHeading(resultHeading, specialty);

            if (resultHeadingTemp.getSpecialtyResultHeadings() == null) {
                resultHeadingTemp.getSpecialtyResultHeadings().add(specialtyResultHeading);
            } else {
                Set<SpecialtyResultHeading> specialtyResultHeadings = new HashSet<SpecialtyResultHeading>();
                specialtyResultHeadings.add(specialtyResultHeading);
                resultHeadingTemp.setSpecialtyResultHeadings(specialtyResultHeadings);
            }

        }

        resultHeadingDao.save(resultHeadingTemp);
    }

    private SpecialtyResultHeading createSpecialtyResultHeading(ResultHeading resultHeading, Specialty specialty) {

        SpecialtyResultHeading specialtyResultHeading = new SpecialtyResultHeading();
        specialtyResultHeading.setResultHeading(resultHeading);
        specialtyResultHeading.setHeading(resultHeading.getHeading());
        specialtyResultHeading.setRollover(resultHeading.getRollover());
        specialtyResultHeading.setPanel(resultHeading.getPanel());
        specialtyResultHeading.setPanelOrder(resultHeading.getPanelorder());
        specialtyResultHeading.setSpecialtyId(specialty.getId().intValue());

        return specialtyResultHeading;
    }

    private ResultHeading getSpecialtyResultHeading(ResultHeading resultHeading, Specialty specialty) {
        if (CollectionUtils.isNotEmpty(resultHeading.getSpecialtyResultHeadings())) {
            SpecialtyResultHeading specResultHeading = getSpecialtyResultHeading(
                    resultHeading.getSpecialtyResultHeadings(), specialty);
            resultHeading.setPanel(specResultHeading.getPanel());
            resultHeading.setPanelorder(specResultHeading.getPanelOrder());
            resultHeading.setRollover(specResultHeading.getRollover());
            resultHeading.setHeading(specResultHeading.getHeading());
        }

        return  resultHeading;

    }

    /**
     * Override the TestResult setting with the specialty settings.
     *
     * @param resultHeadings
     * @return
     */
    private List<ResultHeading> getSpecialtyResultHeadings(List<ResultHeading> resultHeadings, Specialty specialty) {

        for (ResultHeading resultHeading : resultHeadings) {
            if (CollectionUtils.isNotEmpty(resultHeading.getSpecialtyResultHeadings())) {
                SpecialtyResultHeading specResultHeading = getSpecialtyResultHeading(
                        resultHeading.getSpecialtyResultHeadings(), specialty);
                resultHeading.setPanel(specResultHeading.getPanel());
                resultHeading.setPanelorder(specResultHeading.getPanelOrder());
                resultHeading.setRollover(specResultHeading.getRollover());
                resultHeading.setHeading(specResultHeading.getHeading());
            }
        }

        return resultHeadings;

    }

    private SpecialtyResultHeading getSpecialtyResultHeading(Set<SpecialtyResultHeading> specialtyResultHeadings,
                                                             Specialty specialty) {
        for (SpecialtyResultHeading specialtyResultHeading : specialtyResultHeadings) {
            if (specialtyResultHeading.getSpecialtyId() == specialty.getId().intValue()) {
                return specialtyResultHeading;
            }
        }
        return null;
    }

    @Override
    public void delete(String headingCode) {
        resultHeadingDao.delete(headingCode, securityUserManager.getLoggedInSpecialty());
    }

    @Override
    public List<Panel> getPanels() {
        return resultHeadingDao.getPanels(securityUserManager.getLoggedInSpecialty());
    }
}
