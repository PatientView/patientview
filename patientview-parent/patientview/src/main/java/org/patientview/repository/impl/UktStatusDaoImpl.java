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
import org.patientview.patientview.model.UktStatus;
import org.patientview.patientview.model.UktStatus_;
import org.patientview.repository.AbstractHibernateDAO;
import org.patientview.repository.UktStatusDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Repository(value = "uktStatusDao")
public class UktStatusDaoImpl extends AbstractHibernateDAO<UktStatus> implements UktStatusDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AboutmeDaoImpl.class);

    @Inject
    private DataSource dataSource;

    @Override
    public UktStatus get(String nhsno) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UktStatus> criteria = builder.createQuery(UktStatus.class);
        Root<UktStatus> from = criteria.from(UktStatus.class);
        List<Predicate> wherePredicates = new ArrayList<Predicate>();

        wherePredicates.add(builder.equal(from.get(UktStatus_.nhsno), nhsno));

        buildWhereClause(criteria, wherePredicates);
        try {
            return getEntityManager().createQuery(criteria).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public UktStatus getNative(String nhsno) {
        List<UktStatus> uktStatuses = new ArrayList<UktStatus>();

        try {
            Connection connection = dataSource.getConnection();

            String query = "SELECT u.kidney, u.pancreas "
                    + "FROM uktstatus u "
                    + "WHERE u.nhsno = '" + nhsno + "'";

            java.sql.Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(query);

            while ((results.next())) {
                UktStatus uktStatus = new UktStatus();
                uktStatus.setKidney(results.getString(1));
                uktStatus.setPancreas(results.getString(2));
                uktStatuses.add(uktStatus);
            }
        } catch (SQLException se) {
            LOGGER.error("SQLException: ", se);
        }

        if (CollectionUtils.isNotEmpty(uktStatuses)) {
            return uktStatuses.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void deleteAll() {
        Query query = getEntityManager().createNativeQuery("DELETE FROM uktstatus");
        query.executeUpdate();
    }
}
