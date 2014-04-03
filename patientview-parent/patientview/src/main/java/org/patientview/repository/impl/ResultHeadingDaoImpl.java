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

package org.patientview.repository.impl;

import org.patientview.model.Specialty;
import org.patientview.patientview.model.Panel;
import org.patientview.patientview.model.ResultHeading;
import org.patientview.repository.AbstractHibernateDAO;
import org.patientview.repository.ResultHeadingDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * An investment into a framework to execute hibernate queries for this project would probably be worthwhile
 * eg #get(Specialty specialty) and #getAll(Specialty specialty) are the name method with a different word between
 * the two.
 *
 */
@Repository(value = "resultHeadingDao")
public class ResultHeadingDaoImpl extends AbstractHibernateDAO<ResultHeading> implements ResultHeadingDao {

    private JdbcTemplate jdbcTemplate;

    @Inject
    private DataSource dataSource;

    @PostConstruct
    public void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public ResultHeading get(String headingcode, Specialty specialty) {

        StringBuilder hsql = new StringBuilder();

        hsql.append("SELECT   rh ");
        hsql.append("FROM     ResultHeading rh ");
        hsql.append("JOIN     rh.specialtyResultHeadings srh ");
        hsql.append("WHERE    srh.specialtyId = ?");
        hsql.append("AND      rh.headingcode = ?");

        Query query = getEntityManager().createQuery(hsql.toString(), ResultHeading.class);
        query.setParameter(1, specialty.getId().intValue());
        query.setParameter(2, headingcode);

        return (ResultHeading) query.getSingleResult();
    }

    /**
     * This will return the specialties setup for your specialty
     *
     * @param specialty
     * @return
     */
    public List<ResultHeading> get(Specialty specialty) {

        StringBuilder hsql = new StringBuilder();

        hsql.append("SELECT   rh ");
        hsql.append("FROM     ResultHeading rh ");
        hsql.append("JOIN     rh.specialtyResultHeadings srh ");
        hsql.append("WHERE    srh.specialtyId = ?");

        Query query = getEntityManager().createQuery(hsql.toString(), ResultHeading.class);
        query.setParameter(1, specialty.getId().intValue());

        return query.getResultList();
    }


    /**
     * This will be all the result heading and the specialty settings
     *
     * @param specialty
     * @return
     */
    @Override
    public List<ResultHeading> getAll(Specialty specialty) {

        StringBuilder hsql = new StringBuilder();

        hsql.append("SELECT   rh ");
        hsql.append("FROM     ResultHeading rh ");
        hsql.append("lEFT JOIN     rh.specialtyResultHeadings srh ");
        hsql.append("WHERE    srh.specialtyId = ? ");
        hsql.append("OR       srh.specialtyId IS NULL");

        Query query = getEntityManager().createQuery(hsql.toString(), ResultHeading.class);
        query.setParameter(1, specialty.getId().intValue());

        return query.getResultList();
    }


    @Override
    public List<ResultHeading> get(int panel, Specialty specialty) {

        StringBuilder hsql = new StringBuilder();

        hsql.append("SELECT   rh ");
        hsql.append("FROM     ResultHeading rh ");
        hsql.append("JOIN     rh.specialtyResultHeadings srh ");
        hsql.append("WHERE    srh.specialtyId = ?");
        hsql.append("WHERE    srh.panel = ?");

        Query query = getEntityManager().createQuery(hsql.toString(), ResultHeading.class);
        query.setParameter(1, specialty.getId().intValue());
        query.setParameter(2, panel);

        return query.getResultList();
    }

    @Override
    public void delete(String headingCode, Specialty specialty) {
        delete(get(headingCode, specialty));
    }

    @Override
    public List<Panel> getPanels(Specialty specialty) {

        StringBuilder hsql = new StringBuilder();

        hsql.append("SELECT   DISTINCT srh.panel ");
        hsql.append("FROM     specialtyResultHeading srh.");
        hsql.append("WHERE    srh.panel != 0 ");
        hsql.append("AND      srh.specialtyId = ?");
        hsql.append("ORDER BY srh.panel ASC ");

        Query query = getEntityManager().createQuery(hsql.toString());
        query.setParameter(1, specialty.getId().intValue());

        List resultsObjs = query.getResultList();
        List<Panel> results = new ArrayList<Panel>();
        for (Object object : resultsObjs) {
            results.add(new Panel((Integer) object));
        }

        return results;
    }




}
