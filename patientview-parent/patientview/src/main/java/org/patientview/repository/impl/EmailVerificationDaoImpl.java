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

import org.patientview.patientview.model.EmailVerification;
import org.patientview.patientview.model.EmailVerification_;
import org.patientview.repository.AbstractHibernateDAO;
import org.patientview.repository.EmailVerificationDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
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
import java.util.Calendar;
import java.util.List;

@Repository(value = "emailVerificationDao")
public class EmailVerificationDaoImpl extends AbstractHibernateDAO<EmailVerification> implements EmailVerificationDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailVerificationDaoImpl.class);

    @Inject
    private DataSource dataSource;

    @Override
    public List<EmailVerification> get(String verificationCode) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<EmailVerification> criteria = builder.createQuery(EmailVerification.class);
        Root<EmailVerification> emailVerificationRoot = criteria.from(EmailVerification.class);

        List<Predicate> wherePredicates = new ArrayList<Predicate>();

        wherePredicates.add(builder.equal(emailVerificationRoot.get(EmailVerification_.verificationcode),
                verificationCode));

        // TODO: not sure of the greater than and just using Calendar.getInstance()
        wherePredicates.add(builder.greaterThan(emailVerificationRoot.get(EmailVerification_.expirydatestamp),
                                Calendar.getInstance()));

        buildWhereClause(criteria, wherePredicates);

        return getEntityManager().createQuery(criteria).getResultList();
    }

    @Override
    public void delete(String username) {
        // I assume there would be only one in there but creating the statement instead of pulling back
        // all the objects with that username and deleting each one individually
        Query query = getEntityManager().createNativeQuery("DELETE FROM " + EmailVerification.class.getSimpleName()
                + " WHERE " + EmailVerification_.username.getName() + " = :" + EmailVerification_.username.getName());

        query.setParameter(EmailVerification_.username.getName(), username);
        query.executeUpdate();
    }

    @Override
    public List<EmailVerification> getByEmail(String email) {
        String sql = "SELECT ev.* FROM emailverification ev WHERE ev.email = :email";
        Query query = getEntityManager().createNativeQuery(sql, EmailVerification.class);
        query.setParameter("email", email);
        return query.getResultList();
    }

    @Override
    public List<EmailVerification> getByEmailNative(String email) {
        List<EmailVerification> emailVerifications = new ArrayList<EmailVerification>();

        try {
            Connection connection = dataSource.getConnection();

            String query = "SELECT ev.verificationcode "
                    + "FROM emailverification ev "
                    + "WHERE ev.email = '" + email + "'";

            java.sql.Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(query);

            while ((results.next())) {
                EmailVerification emailVerification = new EmailVerification();
                emailVerification.setVerificationcode(results.getString(1));
                emailVerifications.add(emailVerification);
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

        return emailVerifications;
    }
}
