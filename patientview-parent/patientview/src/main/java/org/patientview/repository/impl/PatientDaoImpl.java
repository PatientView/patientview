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

import org.patientview.model.Patient;
import org.patientview.model.Patient_;
import org.patientview.model.Specialty;
import org.patientview.model.enums.SourceType;
import org.patientview.patientview.logon.PatientLogonWithTreatment;
import org.patientview.repository.AbstractHibernateDAO;
import org.patientview.repository.PatientDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository(value = "patientDao")
public class PatientDaoImpl extends AbstractHibernateDAO<Patient> implements PatientDao {

    private JdbcTemplate jdbcTemplate;

    @Inject
    private DataSource dataSource;

    @PostConstruct
    public void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Patient get(Long id) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Patient> criteria = builder.createQuery(Patient.class);
        Root<Patient> from = criteria.from(Patient.class);
        List<Predicate> wherePredicates = new ArrayList<Predicate>();

        wherePredicates.add(builder.equal(from.get(Patient_.id), id));

        buildWhereClause(criteria, wherePredicates);

        try {
            return getEntityManager().createQuery(criteria).getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }


    @Override
    public Patient get(String nhsno, String unitcode) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Patient> criteria = builder.createQuery(Patient.class);
        Root<Patient> from = criteria.from(Patient.class);
        List<Predicate> wherePredicates = new ArrayList<Predicate>();

        wherePredicates.add(builder.equal(from.get(Patient_.nhsno), nhsno));
        wherePredicates.add(builder.equal(from.get(Patient_.unitcode), unitcode));
        wherePredicates.add(builder.equal(from.get(Patient_.sourceType), SourceType.PATIENT_VIEW.getName()));

        buildWhereClause(criteria, wherePredicates);

        try {
            return getEntityManager().createQuery(criteria).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Patient getRadarPatient(String nhsNo) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Patient> criteria = builder.createQuery(Patient.class);
        Root<Patient> from = criteria.from(Patient.class);
        List<Predicate> wherePredicates = new ArrayList<Predicate>();

        wherePredicates.add(builder.equal(from.get(Patient_.nhsno), nhsNo));
        wherePredicates.add(builder.equal(from.get(Patient_.sourceType), SourceType.RADAR.getName()));

        buildWhereClause(criteria, wherePredicates);

        try {
            return getEntityManager().createQuery(criteria).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Patient> getByNhsNo(String nhsNo) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Patient> criteria = builder.createQuery(Patient.class);
        Root<Patient> from = criteria.from(Patient.class);
        List<Predicate> wherePredicates = new ArrayList<Predicate>();

        wherePredicates.add(builder.equal(from.get(Patient_.nhsno), nhsNo));

        buildWhereClause(criteria, wherePredicates);

        try {
            return getEntityManager().createQuery(criteria).getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        }

    }

    @Override
    public List<Patient> getByNhsNo(String nhsNo, Specialty specialty) {

        StringBuilder queryText = new StringBuilder();
        queryText.append("SELECT    ptt ");
        queryText.append("FROM      Patient AS ptt ");
        queryText.append(",         Unit AS uni ");
        queryText.append("WHERE     ptt.nhsno = :nhsNo ");
        queryText.append("AND       ptt.unitcode = uni.unitcode ");
        queryText.append("AND       uni.specialty.id = :specialtyId ");
        queryText.append("GROUP BY  ptt.nhsno");

        TypedQuery<Patient> query = getEntityManager().createQuery(queryText.toString(), Patient.class);
        query.setParameter("nhsNo", nhsNo);
        query.setParameter("specialtyId", specialty.getId());

        try {
            return query.getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public void delete(String nhsno, String unitcode) {
        // TODO Change this for 1.3
        if (nhsno == null || nhsno.length() == 0 || unitcode == null || unitcode.length() == 0) {
            throw new IllegalArgumentException("Required parameters nhsno and unitcode to delete patient");
        }

        Patient patient = get(nhsno, unitcode);

        if (patient != null) {
            delete(patient);
        }
    }

    @Override
    public List<Patient> get(String centreCode) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Patient> criteria = builder.createQuery(Patient.class);
        Root<Patient> from = criteria.from(Patient.class);
        List<Predicate> wherePredicates = new ArrayList<Predicate>();

        wherePredicates.add(builder.equal(from.get(Patient_.unitcode), centreCode));

        buildWhereClause(criteria, wherePredicates);
        return getEntityManager().createQuery(criteria).getResultList();
    }

    //todo refactor into one query with the one below
    //todo PERFORMANCE FIX: commented out the emailverification table to improve query speed.
    // todo PERFORMANCE FIX & GENERAL BUG: removed the left join to the pv_user_log, need to reimplement
    @Override
    public List getUnitPatientsWithTreatmentDao(String unitcode, String nhsno, String firstname, String lastname,
                                                boolean showgps, Specialty specialty, boolean includeHidden) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT    usr.username ");
        query.append(",         usr.password ");
        query.append(",         usr.firstname ");
        query.append(",         usr.lastname ");
        query.append(",         usr.email ");
        query.append(",         usr.emailverified ");
        query.append(",         usr.accountlocked ");
        query.append(",         usr.accounthidden ");
        query.append(",         usm.nhsno ");
        query.append(",         usm.unitcode ");
        query.append(",         null lastverificationdate ");
        query.append(",         usr.firstlogon ");
        query.append(",         usr.lastlogon ");
        query.append(",         MAX(ptt.id) id ");
        query.append(",         MAX(ptt.treatment) treatment ");
        query.append(",         MAX(ptt.dateofbirth) dateofbirth ");
        query.append(",         MAX(ptt.rrtModality) rrtModality ");
        query.append(",         MAX(ptt.mostRecentTestResultDateRangeStopDate) mostRecentTestResultDateRangeStopDate ");
        query.append("FROM USER usr ");
        query.append("INNER JOIN usermapping usm ON usm.username = usr.username ");
        query.append("LEFT JOIN patient ptt ON usm.nhsno = ptt.nhsno ");
        query.append("INNER JOIN specialtyuserrole str ON str.user_id = usr.id ");
        query.append("WHERE     str.role = 'patient' ");
        query.append("AND       usr.username = usm.username ");
        query.append("AND       usr.id = str.user_id ");
        query.append("AND       usm.unitcode <> 'PATIENT' ");
        query.append("AND       IF(ptt.patientLinkId = 0, NULL, ptt.patientLinkId) IS NULL ");
        query.append("AND       usm.unitcode = ?  ");
        if (StringUtils.hasText(nhsno)) {
            query.append("AND   usm.nhsno LIKE ? ");
        }
        if (StringUtils.hasText(firstname)) {
            query.append("AND   usr.firstname LIKE ? ");
        }
        if (StringUtils.hasText(lastname)) {
            query.append("AND   usr.lastname LIKE ? ");
        }
        if (!showgps) {
            query.append("AND   usr.username NOT LIKE '%-GP' ");
        }
        query.append("AND       str.specialty_id = ? ");
        if (!includeHidden) {
            query.append("AND       usr.accounthidden = false ");
        }
        query.append("GROUP BY  usr.username ");
        query.append(",         usr.password ");
        query.append(",         usr.firstname ");
        query.append(",         usr.lastname ");
        query.append(",         usr.email ");
        query.append(",         usr.emailverified ");
        query.append(",         usr.accountlocked ");
        query.append(",         usm.nhsno ");
        query.append(",         usm.unitcode ");
        query.append(",         lastverificationdate ");
        query.append(",         usr.firstlogon ");
        query.append(",         usr.lastlogon  ");
        query.append(" ORDER BY usr.lastname, usr.firstname ASC ");


        List<Object> params = new ArrayList<Object>();

        params.add(unitcode);

        if (StringUtils.hasText(nhsno)) {
            params.add('%' + nhsno + '%');
        }

        if (StringUtils.hasText(firstname)) {
            params.add('%' + firstname + '%');
        }

        if (StringUtils.hasText(lastname)) {
            params.add('%' + lastname + '%');
        }
        params.add(specialty.getId());

        return jdbcTemplate.query(query.toString(), params.toArray(), new PatientLogonWithTreatmentExtendMapper());
    }

    //todo refactor into one query with the one above
    //todo PERFORMANCE FIX: commented out the emailverification table to improve query speed.
    // todo PERFORMANCE FIX & GENERAL BUG: removed the left join to the pv_user_log, need to reimplement
    @Override
    public List getAllUnitPatientsWithTreatmentDao(String nhsno, String firstname, String lastname, boolean showgps,
                                                   Specialty specialty, boolean includeHidden) {

        StringBuilder query = new StringBuilder();
        query.append("SELECT usr.username ");
        query.append(",      usr.password ");
        query.append(",      usr.firstname ");
        query.append(",      usr.lastname ");
        query.append(",      usr.email ");
        query.append(",      usr.emailverified ");
        query.append(",      usr.accountlocked ");
        query.append(",      usr.accounthidden ");
        query.append(",      MAX(ptt.nhsno) nhsno ");
        query.append(",      MAX(ptt.unitcode) unitcode ");
        query.append(",      null lastverificationdate ");
        query.append(",      usr.firstlogon ");
        query.append(",      usr.lastlogon ");
        query.append(",      MAX(ptt.id) id ");
        query.append(",      MAX(ptt.treatment) treatment ");
        query.append(",      MAX(ptt.dateofbirth) dateofbirth ");
        query.append(",      MAX(ptt.rrtModality) rrtModality  ");
        query.append(",      MAX(ptt.mostRecentTestResultDateRangeStopDate) mostRecentTestResultDateRangeStopDate ");
        query.append("FROM user usr ");
        query.append("INNER JOIN usermapping usm ON usm.username = usr.username ");
        query.append("LEFT  JOIN patient ptt ON usm.nhsno = ptt.nhsno ");
        query.append("INNER JOIN specialtyuserrole str ON str.user_id = usr.id ");
        query.append("WHERE  str.role = 'patient' ");
        query.append("AND    usr.id = str.user_id ");
        if (!includeHidden) {
            query.append("AND    usr.accounthidden = false ");
        }
        query.append("AND    usm.unitcode <> 'PATIENT' ");
        query.append("AND    ptt.nhsno IS NOT NULL ");
        query.append("AND    IF(ptt.patientLinkId = 0, NULL, ptt.patientLinkId) IS NULL ");
        if (nhsno != null && nhsno.length() > 0) {
            query.append("AND usm.nhsno LIKE ? ");
        }
        if (StringUtils.hasText(firstname)) {
            query.append("AND usr.firstname LIKE ? ");
        }
        if (StringUtils.hasText(lastname)) {
            query.append("AND usr.lastname LIKE ? ");
        }
        if (!showgps) {
            query.append("AND usr.username NOT LIKE '%-GP' ");
        }
        query.append("AND    str.specialty_id = ? ");

        query.append("GROUP BY  usr.username ");
        query.append(",         usr.password ");
        query.append(",         usr.firstname ");
        query.append(",         usr.lastname ");
        query.append(",         usr.email ");
        query.append(",         usr.emailverified ");
        query.append(",         usr.accountlocked ");
        query.append(",         usr.firstlogon ");
        query.append(",         usr.lastlogon ");
        query.append(" ORDER BY usr.lastname, usr.firstname ASC  ");


        List<Object> params = new ArrayList<Object>();

        if (nhsno != null && nhsno.length() > 0) {
            params.add('%' + nhsno + '%');
        }

        if (StringUtils.hasText(firstname)) {
            params.add('%' + firstname + '%');
        }

        if (StringUtils.hasText(lastname)) {
            params.add('%' + lastname + '%');
        }
        params.add(specialty.getId());

        return jdbcTemplate.query(query.toString(), params.toArray(), new PatientLogonWithTreatmentExtendMapper());

    }

    @Override
    public List<PatientLogonWithTreatment> getUnitPatientsAllWithTreatmentDao(String unitcode, Specialty specialty,
                                                                              boolean includeHidden) {
        String sql = "SELECT "
                + "   user.username,  "
                + "   user.password, "
                + "   user.firstname, "
                + "   user.lastname, "
                + "   user.email, "
                + "   user.emailverified, "
                + "   user.lastlogon, "
                + "   usermapping.nhsno, "
                + "   usermapping.unitcode, "
                + "   user.firstlogon, "
                + "   user.accountlocked, "
                + "   user.accounthidden, "
                + "   patient.treatment, "
                + "   patient.dateofbirth, "
                + "   patient.id "
                + "FROM "
                + "   user, "
                + "   specialtyuserrole, "
                + "   usermapping "
                + "LEFT JOIN "
                + "   patient ON usermapping.nhsno = patient.nhsno "
                + "WHERE "
                + "   usermapping.username = user.username "
                + "AND "
                + "   user.id = specialtyuserrole.user_id "
                + "AND "
                + "   usermapping.unitcode = ? "
                + "AND "
                + "   specialtyuserrole.role = 'patient' "
                + "AND "
                + "   user.username NOT LIKE '%-GP' ";

        if (!includeHidden) {
            sql += "AND user.accounthidden = false ";
        }

        sql     += "AND   specialtyuserrole.specialty_id = ? "
                + "ORDER BY "
                + "   user.lastname, user.firstname ASC";

        List<Object> params = new ArrayList<Object>();

        params.add(unitcode);
        params.add(specialty.getId());

        return jdbcTemplate.query(sql, params.toArray(), new PatientLogonWithTreatmentMapper());
    }

    @Override
    public List<Patient> getUktPatients() {

        String sql = "SELECT DISTINCT patient.nhsno, patient.surname, patient.forename, "
                + " patient.dateofbirth, patient.postcode, patient.id FROM patient, user, usermapping "
                + " WHERE patient.nhsno REGEXP '^[0-9]{10}$' AND patient.nhsno = usermapping.nhsno "
                + "AND user.username = usermapping.username "
                + "AND user.accounthidden = false "
                + " AND usermapping.username NOT LIKE '%-GP' AND user.dummypatient = 0";

        return jdbcTemplate.query(sql, new PatientMapper());
    }

    private class PatientMapper implements RowMapper<Patient> {

        @Override
        public Patient mapRow(ResultSet resultSet, int i) throws SQLException {

            Patient patient = new Patient();
            patient.setNhsno(resultSet.getString("nhsno"));
            patient.setSurname(resultSet.getString("surname"));
            patient.setForename(resultSet.getString("forename"));
            patient.setDateofbirth(resultSet.getDate("dateofbirth"));
            patient.setPostcode(resultSet.getString("postcode"));

            return patient;
        }
    }

    private class PatientLogonWithTreatmentMapper implements RowMapper<PatientLogonWithTreatment> {
        @Override
        public PatientLogonWithTreatment mapRow(ResultSet resultSet, int i) throws SQLException {
            PatientLogonWithTreatment patientLogonWithTreatment = new PatientLogonWithTreatment();

            patientLogonWithTreatment.setUsername(resultSet.getString("username"));
            patientLogonWithTreatment.setPassword(resultSet.getString("password"));
            patientLogonWithTreatment.setFirstName(resultSet.getString("firstName"));
            patientLogonWithTreatment.setLastName(resultSet.getString("lastName"));
            patientLogonWithTreatment.setEmail(resultSet.getString("email"));
            patientLogonWithTreatment.setEmailverified(resultSet.getBoolean("emailverified"));
            patientLogonWithTreatment.setAccountlocked(resultSet.getBoolean("accountlocked"));
            patientLogonWithTreatment.setNhsno(resultSet.getString("nhsno"));
            patientLogonWithTreatment.setFirstlogon(resultSet.getBoolean("firstlogon"));
            patientLogonWithTreatment.setLastlogon(resultSet.getDate("lastlogon"));
            patientLogonWithTreatment.setUnitcode(resultSet.getString("unitcode"));
            patientLogonWithTreatment.setTreatment(resultSet.getString("treatment"));
            patientLogonWithTreatment.setDateofbirth(resultSet.getDate("dateofbirth"));
            patientLogonWithTreatment.setPatientId(resultSet.getLong("id"));
            patientLogonWithTreatment.setAccounthidden(resultSet.getBoolean("accounthidden"));
            return patientLogonWithTreatment;
        }
    }

    private class PatientLogonWithTreatmentExtendMapper extends PatientLogonWithTreatmentMapper {
        @Override
        public PatientLogonWithTreatment mapRow(ResultSet resultSet, int i) throws SQLException {
            PatientLogonWithTreatment patientLogonWithTreatment = super.mapRow(resultSet, i);
            patientLogonWithTreatment.setPatientId(resultSet.getLong("id"));
            patientLogonWithTreatment.setLastverificationdate(resultSet.getDate("lastverificationdate"));
            patientLogonWithTreatment.setRrtModality(resultSet.getInt("rrtModality"));
            patientLogonWithTreatment.setLastdatadate(resultSet.getDate("mostRecentTestResultDateRangeStopDate"));
            return patientLogonWithTreatment;
        }
    }
}
