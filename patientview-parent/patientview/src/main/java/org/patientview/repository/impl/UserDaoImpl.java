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
import org.patientview.model.Unit;
import org.patientview.patientview.model.User;
import org.patientview.patientview.model.User_;
import org.patientview.repository.AbstractHibernateDAO;
import org.patientview.repository.UserDao;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository(value = "userDao")
public class UserDaoImpl extends AbstractHibernateDAO<User> implements UserDao {

    @Override
    public List<User> get(String nhsno, String unitcode) {
        StringBuilder hsql = new StringBuilder();

        hsql.append("SELECT   DISTINCT user ");
        hsql.append("FROM     UserMapping usm ");
        hsql.append("WHERE    usm.nhsno = :nhsno ");
        hsql.append("AND      usm.unitcode = :unitcode ");

        Query query = getEntityManager().createQuery(hsql.toString(), User.class);
        query.setParameter("unitcode", unitcode);
        query.setParameter("nhsno", nhsno);

        return query.getResultList();
    }

    @Override
    public List<User> getByUnitcode(String unitcode) {
        StringBuilder hsql = new StringBuilder();

        hsql.append("SELECT   DISTINCT user ");
        hsql.append("FROM     UserMapping usm ");
        hsql.append("WHERE    usm.unitcode = :unitcode ");

        Query query = getEntityManager().createQuery(hsql.toString(), User.class);
        query.setParameter("unitcode", unitcode);

        return query.getResultList();
    }

    @Override
    public List<Long> getIdsByUnitcode(String unitcode) {
        StringBuilder hsql = new StringBuilder();

        hsql.append("SELECT   DISTINCT user.id ");
        hsql.append("FROM     UserMapping usm ");
        hsql.append("WHERE    usm.unitcode = :unitcode ");

        Query query = getEntityManager().createQuery(hsql.toString(), Long.class);
        query.setParameter("unitcode", unitcode);

        return query.getResultList();
    }

    @Override
    public List<Long> getIdsByUnitcodeNoGp(String unitcode) {
        StringBuilder hsql = new StringBuilder();

        hsql.append("SELECT   DISTINCT u.id ");
        hsql.append("FROM     UserMapping usm, User u ");
        hsql.append("WHERE    usm.unitcode = :unitcode ");
        hsql.append("AND      usm.username = u.username ");
        hsql.append("AND      u.username NOT LIKE '%-GP' ");

        Query query = getEntityManager().createQuery(hsql.toString(), Long.class);
        query.setParameter("unitcode", unitcode);

        return query.getResultList();
    }

    @Override
    public User get(String username) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> userRoot = criteria.from(User.class);
        criteria.where(builder.equal(userRoot.get(User_.username), username));

        try {
            return getEntityManager().createQuery(criteria).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<User> get(User user, Specialty specialty, String userType, Unit unit) {
        String sql = "SELECT "
                + "  u.* "
                + "FROM "
                + "  User u, "
                + "  UserMapping um, "
                + "  SpecialtyUserRole sur "
                + "WHERE u.username = um.username "
                + "AND u.id = sur.user_id "
                + "AND sur.role = :userType "
                + "AND u.username NOT LIKE '%-GP%' "
                + "AND um.unitcode = :unitcode  "
                + "AND sur.specialty_id = :specialtyId ";

        Query query = getEntityManager().createNativeQuery(sql, User.class);

        query.setParameter("userType", userType);
        query.setParameter("specialtyId", specialty.getId());
        query.setParameter("unitcode", unit.getUnitcode());

        return query.getResultList();
    }

    @Override
    public List<User> getByEmailAddress(String emailAddress) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> userRoot = criteria.from(User.class);
        criteria.where(builder.equal(userRoot.get(User_.email), emailAddress));

        try {
            return getEntityManager().createQuery(criteria).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<String> getEcrPatientIdentifiers() {
        String sql = "SELECT "
                + "  ump.nhsno "
                + "FROM "
                + "  User usr, "
                + "  UserMapping ump, "
                + "  Unit uni "
                + "WHERE usr.username = ump.username "
                + "AND ump.unitcode = uni.unitcode "
                + "AND usr.ecrOptInStatus = true "
                + "AND uni.ecrEnabled = true "
                + "AND ump.nhsno IS NOT NULL "
                + "GROUP BY ump.nhsno ";

        Query query = getEntityManager().createQuery(sql);
        return query.getResultList();
    }

    @Override
    public boolean getEcrEnabled(User user) {
        String sql = "SELECT "
                + "  usr.* "
                + "FROM "
                + "  User usr, "
                + "  UserMapping ump, "
                + "  Unit uni "
                + "WHERE usr.username = ump.username "
                + "AND usr.username = :username "
                + "AND ump.unitcode = uni.unitcode "
                + "AND uni.ecrEnabled = true ";

        try {
            Query query = getEntityManager().createNativeQuery(sql, User.class);
            query.setParameter("username", user.getUsername());
            User foundUser = (User) query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
