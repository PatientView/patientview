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
import org.patientview.patientview.model.User;
import org.patientview.patientview.model.UserMapping;
import org.patientview.patientview.model.UserMapping_;
import org.patientview.patientview.unit.UnitUtils;
import org.patientview.repository.AbstractHibernateDAO;
import org.patientview.repository.UserMappingDao;
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
import java.util.List;

/**
 *
 */
@Repository(value = "userMappingDao")
public class UserMappingDaoImpl extends AbstractHibernateDAO<UserMapping> implements UserMappingDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserMappingDaoImpl.class);

    @Inject
    private DataSource dataSource;

    private static final int THREE = 3;

    @Override
    public void deleteUserMappings(String username, String unitcode, Specialty specialty) {

        List<UserMapping> userMappings = getAll(username, unitcode, specialty);

        try {
            for (UserMapping userMapping : userMappings) {
                delete(userMapping);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.debug(e.getMessage(), e);
        }
    }


    /**
     * Another native call to get round updateable and insertable flags on the entity. Need to swop Username for User_id
     * in the model to solve this
     *
     * @param userMapping
     */
    public void save(UserMapping userMapping) {
        if (userMapping.hasValidId()) {
            super.save(userMapping);
        } else {

            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO   usermapping (username, unitcode, nhsno, specialty_id) ");
            sql.append("VALUES (:username, :unitcode, :nhsno, :specialty_id)");


            Query query = getEntityManager().createNativeQuery(sql.toString());

            query.setParameter("username", userMapping.getUsername());
            query.setParameter("unitcode", userMapping.getUnitcode());
            query.setParameter("nhsno", userMapping.getNhsno());
            if (userMapping.getSpecialty() != null) {
                query.setParameter("specialty_id", userMapping.getSpecialty().getId());
            } else {
                query.setParameter("specialty_id", null);
            }

            query.executeUpdate();
        }

    }

    /**
     * This is because of the model. Hibernate will not let you update a foreign key
     *
     * @param newUsername
     * @param oldUsername
     */
    @Override
    public void updateUsername(String newUsername, String oldUsername) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE   usermapping ");
        sql.append("SET      username = :newUsername ");
        sql.append("WHERE    username = :oldUsername ");


        Query query = getEntityManager().createNativeQuery(sql.toString());

        query.setParameter("newUsername", newUsername);
        query.setParameter("oldUsername", oldUsername);
        query.executeUpdate();

    }

    @Override
    public List<UserMapping> getAll(String username) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UserMapping> criteria = builder.createQuery(UserMapping.class);
        Root<UserMapping> from = criteria.from(UserMapping.class);
        List<Predicate> wherePredicates = new ArrayList<Predicate>();

        wherePredicates.add(builder.equal(from.get(UserMapping_.username), username));

        buildWhereClause(criteria, wherePredicates);
        return getEntityManager().createQuery(criteria).getResultList();
    }

    @Override
    public List<UserMapping> getAllNative(String username) {
        List<UserMapping> userMappings = new ArrayList<UserMapping>();

        // handle ' in name e.g. O'Donnell
        username = username.replace("'", "''");

        try {
            Connection connection = dataSource.getConnection();

            String query = "SELECT um.username, um.unitcode, um.nhsno "
                    + "FROM usermapping um "
                    + "WHERE um.username = '" + username + "'";

            java.sql.Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(query);

            while ((results.next())) {
                UserMapping userMapping = new UserMapping();
                userMapping.setUsername(results.getString(1));
                userMapping.setUnitcode(results.getString(2));
                userMapping.setNhsno(results.getString(THREE));
                userMappings.add(userMapping);
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

        return userMappings;
    }

    @Override
    public List<UserMapping> getAll(String username, Specialty specialty) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UserMapping> criteria = builder.createQuery(UserMapping.class);
        Root<UserMapping> from = criteria.from(UserMapping.class);
        List<Predicate> wherePredicates = new ArrayList<Predicate>();

        wherePredicates.add(builder.equal(from.get(UserMapping_.username), username));
        wherePredicates.add(builder.equal(from.get(UserMapping_.specialty), specialty));

        buildWhereClause(criteria, wherePredicates);
        return getEntityManager().createQuery(criteria).getResultList();
    }

    @Override
    public List<UserMapping> getAll(String username, String unitcode) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UserMapping> criteria = builder.createQuery(UserMapping.class);
        Root<UserMapping> from = criteria.from(UserMapping.class);
        List<Predicate> wherePredicates = new ArrayList<Predicate>();

        wherePredicates.add(builder.equal(from.get(UserMapping_.username), username));
        wherePredicates.add(builder.equal(from.get(UserMapping_.unitcode), unitcode));

        buildWhereClause(criteria, wherePredicates);
        return getEntityManager().createQuery(criteria).getResultList();
    }

    @Override
    public List<UserMapping> getAllExcludeUnitcode(String username, String unitcode, Specialty specialty) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UserMapping> criteria = builder.createQuery(UserMapping.class);
        Root<UserMapping> from = criteria.from(UserMapping.class);
        List<Predicate> wherePredicates = new ArrayList<Predicate>();

        wherePredicates.add(builder.equal(from.get(UserMapping_.username), username));
        wherePredicates.add(builder.notEqual(from.get(UserMapping_.unitcode), unitcode));
        wherePredicates.add(builder.equal(from.get(UserMapping_.specialty), specialty));

        buildWhereClause(criteria, wherePredicates);
        return getEntityManager().createQuery(criteria).getResultList();
    }

    @Override
    public List<UserMapping> getAll(String username, String unitcode, Specialty specialty) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UserMapping> criteria = builder.createQuery(UserMapping.class);
        Root<UserMapping> from = criteria.from(UserMapping.class);
        List<Predicate> wherePredicates = new ArrayList<Predicate>();

        wherePredicates.add(builder.equal(from.get(UserMapping_.username), username));
        wherePredicates.add(builder.equal(from.get(UserMapping_.unitcode), unitcode));
        wherePredicates.add(builder.equal(from.get(UserMapping_.specialty), specialty));

        buildWhereClause(criteria, wherePredicates);
        return getEntityManager().createQuery(criteria).getResultList();
    }

    @Override
    public List<UserMapping> getAllByNhsNo(String nhsNo) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UserMapping> criteria = builder.createQuery(UserMapping.class);
        Root<UserMapping> from = criteria.from(UserMapping.class);
        List<Predicate> wherePredicates = new ArrayList<Predicate>();

        wherePredicates.add(builder.equal(from.get(UserMapping_.nhsno), nhsNo));

        buildWhereClause(criteria, wherePredicates);
        return getEntityManager().createQuery(criteria).getResultList();
    }

    @Override
    public List<UserMapping> getAllByNhsNo(String nhsNo, String unitCode) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UserMapping> criteria = builder.createQuery(UserMapping.class);
        Root<UserMapping> from = criteria.from(UserMapping.class);
        List<Predicate> wherePredicates = new ArrayList<Predicate>();

        wherePredicates.add(builder.equal(from.get(UserMapping_.nhsno), nhsNo));
        wherePredicates.add(builder.equal(from.get(UserMapping_.unitcode), unitCode));

        buildWhereClause(criteria, wherePredicates);
        return getEntityManager().createQuery(criteria).getResultList();
    }

    @Override
    public List<UserMapping> getAllByNhsNo(String nhsNo, Specialty specialty) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UserMapping> criteria = builder.createQuery(UserMapping.class);
        Root<UserMapping> from = criteria.from(UserMapping.class);
        List<Predicate> wherePredicates = new ArrayList<Predicate>();

        wherePredicates.add(builder.equal(from.get(UserMapping_.nhsno), nhsNo));
        wherePredicates.add(builder.equal(from.get(UserMapping_.specialty), specialty));

        buildWhereClause(criteria, wherePredicates);
        return getEntityManager().createQuery(criteria).getResultList();
    }

    @Override
    public List<UserMapping> getAllByUnitcode(String unitcode) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UserMapping> criteria = builder.createQuery(UserMapping.class);
        Root<UserMapping> from = criteria.from(UserMapping.class);
        List<Predicate> wherePredicates = new ArrayList<Predicate>();

        wherePredicates.add(builder.equal(from.get(UserMapping_.unitcode), unitcode));

        buildWhereClause(criteria, wherePredicates);
        return getEntityManager().createQuery(criteria).getResultList();
    }

    @Override
    public String getUsersRealUnitcodeBestGuess(String username, Specialty specialty) {

        List<UserMapping> userMappings = getAllExcludeUnitcode(username, UnitUtils.PATIENT_ENTERS_UNITCODE, specialty);

        if (userMappings == null || userMappings.isEmpty()) {
            return "";
        } else {
            return userMappings.get(0).getUnitcode();
        }
    }

    @Override
    public String getUsersRealNhsNoBestGuess(String username, Specialty specialty) {

        List<UserMapping> userMappings = getAllExcludeUnitcode(username, UnitUtils.PATIENT_ENTERS_UNITCODE, specialty);

        if (userMappings == null || userMappings.isEmpty()) {
            return "";
        } else {
            return userMappings.get(0).getNhsno();
        }
    }

    @Override
    public UserMapping getUserMappingPatientEntered(User user, Specialty specialty) {

        List<UserMapping> userMappings = getAll(user.getUsername(), specialty);

        UserMapping patientEntryUserMapping = null;
        UserMapping anyOtherUserMapping = null;

        for (UserMapping currentUserMapping : userMappings) {

            if (UnitUtils.PATIENT_ENTERS_UNITCODE.equals(currentUserMapping.getUnitcode())) {
                patientEntryUserMapping = currentUserMapping;
            } else {
                anyOtherUserMapping = currentUserMapping;
            }
        }

        if (patientEntryUserMapping == null) {
            if (anyOtherUserMapping != null) {
                patientEntryUserMapping = new UserMapping(anyOtherUserMapping.getUsername(),
                        UnitUtils.PATIENT_ENTERS_UNITCODE, anyOtherUserMapping.getNhsno());
                patientEntryUserMapping.setSpecialty(specialty);
                save(patientEntryUserMapping);
                return patientEntryUserMapping;
            }
        } else {
            return patientEntryUserMapping;
        }

        return null;
    }

    @Override
    public List<UserMapping> getUsersSiblings(String username, String unitcode, Specialty specialty) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UserMapping> criteria = builder.createQuery(UserMapping.class);
        Root<UserMapping> from = criteria.from(UserMapping.class);
        List<Predicate> wherePredicates = new ArrayList<Predicate>();

        Predicate username1 = builder.equal(from.get(UserMapping_.username), username);
        Predicate username2 = builder.equal(from.get(UserMapping_.username), username + "-GP");

        Predicate unitCode1 = builder.equal(from.get(UserMapping_.unitcode), unitcode);
        Predicate unitCode2 = builder.equal(from.get(UserMapping_.unitcode), "PATIENT");

        wherePredicates.add(builder.or(username1, username2));
        wherePredicates.add(builder.or(unitCode1, unitCode2));
        wherePredicates.add(builder.equal(from.get(UserMapping_.specialty), specialty));

        buildWhereClause(criteria, wherePredicates);
        return getEntityManager().createQuery(criteria).getResultList();
    }

    @Override
    public List<UserMapping> getDuplicateUsers(String nhsno, String username, Specialty specialty) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UserMapping> criteria = builder.createQuery(UserMapping.class);
        Root<UserMapping> from = criteria.from(UserMapping.class);
        List<Predicate> wherePredicates = new ArrayList<Predicate>();

        wherePredicates.add(builder.equal(from.get(UserMapping_.nhsno), nhsno));
        wherePredicates.add(builder.notEqual(from.get(UserMapping_.username), username));
        wherePredicates.add(builder.notLike(from.get(UserMapping_.username), "%-GP"));
        wherePredicates.add(builder.equal(from.get(UserMapping_.specialty), specialty));

        buildWhereClause(criteria, wherePredicates);

        return getEntityManager().createQuery(criteria).getResultList();
    }
}
