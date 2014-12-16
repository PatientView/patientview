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

import org.apache.commons.collections.CollectionUtils;
import org.patientview.patientview.model.Aboutme;
import org.patientview.patientview.model.Aboutme_;
import org.patientview.repository.AboutmeDao;
import org.patientview.repository.AbstractHibernateDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository(value = "aboutmeDao")
public class AboutmeDaoImpl extends AbstractHibernateDAO<Aboutme> implements AboutmeDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AboutmeDaoImpl.class);
    private static final int THREE = 3;

    @Inject
    private DataSource dataSource;

    @Override
    public Aboutme get(String nhsno) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Aboutme> criteria = builder.createQuery(Aboutme.class);
        Root<Aboutme> aboutmeRoot = criteria.from(Aboutme.class);

        criteria.where(builder.equal(aboutmeRoot.get(Aboutme_.nhsno), nhsno));

        // safer, as possible there may be multiple
        List results = getEntityManager().createQuery(criteria).getResultList();
        Aboutme foundAboutMe = null;
        if (!results.isEmpty()) {
            foundAboutMe = (Aboutme) results.get(0);
        }

        return foundAboutMe;
    }

    @Override
    public Aboutme getNative(String nhsno) {
        List<Aboutme> aboutmes = new ArrayList<Aboutme>();

        try {
            Connection connection = dataSource.getConnection();

            String query = "SELECT a.nhsno, a.aboutme, a.talkabout "
                    + "FROM aboutme a "
                    + "WHERE a.nhsno = '" + nhsno + "'";

            java.sql.Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(query);

            while ((results.next())) {
                Aboutme aboutme = new Aboutme();
                aboutme.setNhsno(results.getString(1));
                aboutme.setAboutme(results.getString(2));
                aboutme.setTalkabout(results.getString(THREE));
                aboutmes.add(aboutme);
            }
        } catch (SQLException se) {
            LOGGER.error("SQLException: ", se);
        }

        if (CollectionUtils.isNotEmpty(aboutmes)) {
            return aboutmes.get(0);
        } else {
            return null;
        }
    }
}
