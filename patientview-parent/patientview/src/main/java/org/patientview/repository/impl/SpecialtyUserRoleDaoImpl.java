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

import org.patientview.patientview.model.SpecialtyUserRole;
import org.patientview.patientview.model.SpecialtyUserRole_;
import org.patientview.patientview.model.User;
import org.patientview.repository.AbstractHibernateDAO;
import org.patientview.repository.SpecialtyUserRoleDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
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
@Repository(value = "specialtyUserRoleDao")
public class SpecialtyUserRoleDaoImpl extends AbstractHibernateDAO<SpecialtyUserRole>
        implements SpecialtyUserRoleDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecialtyUserRoleDaoImpl.class);

    @Inject
    private DataSource dataSource;

    @Override
    public List<SpecialtyUserRole> get(User user) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SpecialtyUserRole> criteria = builder.createQuery(SpecialtyUserRole.class);
        Root<SpecialtyUserRole> from = criteria.from(SpecialtyUserRole.class);
        List<Predicate> wherePredicates = new ArrayList<Predicate>();

        wherePredicates.add(builder.equal(from.get(SpecialtyUserRole_.user), user));

        buildWhereClause(criteria, wherePredicates);
        return getEntityManager().createQuery(criteria).getResultList();
    }

    @Override
    public List<SpecialtyUserRole> getRolesNative(Long userId) {
        List<SpecialtyUserRole> specialtyUserRoles = new ArrayList<SpecialtyUserRole>();

        try {
            Connection connection = dataSource.getConnection();

            String query = "SELECT sur.role "
                    + "FROM specialtyuserrole sur "
                    + "WHERE sur.user_id = " + userId;

            java.sql.Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(query);

            while ((results.next())) {
                SpecialtyUserRole specialtyUserRole = new SpecialtyUserRole();
                specialtyUserRole.setRole(results.getString(1));
                specialtyUserRoles.add(specialtyUserRole);
            }
            // try and close the open connection
            try {
                connection.close();
            } catch (SQLException e2) {
                LOGGER.error("Cannot close connection {}", e2);
            }
        } catch (SQLException se) {
            LOGGER.error("SQLException: ", se);
        }

        return specialtyUserRoles;
    }
}
