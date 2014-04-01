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

package org.patientview.radar.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.patientview.radar.dao.IssueDao;
import org.patientview.radar.model.Issue;
import org.patientview.radar.model.enums.IssuePriority;
import org.patientview.radar.model.enums.IssueStatus;
import org.patientview.radar.model.enums.IssueType;
import org.patientview.radar.model.filter.IssueFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IssueDaoImpl extends BaseDaoImpl implements IssueDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(IssueDaoImpl.class);

    private SimpleJdbcInsert issuesInsert;

    @Override
    public void setDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);

        // Initialise a simple JDBC insert to be able to get the allocated ID
        issuesInsert = new SimpleJdbcInsert(dataSource).withTableName("tbl_IssueTracker")
                .usingGeneratedKeyColumns("bID")
                .usingColumns("bType", "bPage", "bDateLogged", "bDateResolved", "bDesc", "bComment", "bPriority",
                        "bStatus", "bUpdated");
    }

    public Issue getIssue(Long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM tbl_IssueTracker WHERE bID = ?",
                    new Object[]{id}, new IssueRowMapper());
        } catch (EmptyResultDataAccessException e) {
            LOGGER.debug("Could not find row in table tbl_IssueTracker with bID {}", id);
        }
        return null;
    }

    public void saveIssue(final Issue issue) throws Exception {
       Map<String, Object> issueMap = new HashMap<String, Object>() {
            {
                put("bType", issue.getType().getName());
                put("bPage", issue.getPage());
                put("bDateLogged", issue.getDateLogged());
                put("bDateResolved", issue.getDateResolved());
                put("bDesc", issue.getDescription());
                put("bComment", issue.getComments());
                put("bPriority", issue.getPriority().getName());
                put("bStatus", issue.getStatus().getName());
                put("bUpdated", issue.getUpdated());
                put("bID", issue.getId());
            }
        };

        if (issue.hasValidId()) {
            String updateSql = buildUpdateQuery("tbl_IssueTracker", "bID", issueMap);
            namedParameterJdbcTemplate.update(updateSql, issueMap);
        } else {
            Number id = issuesInsert.executeAndReturnKey(issueMap);
            issue.setId(id.longValue());
        }
    }

    public void deleteIssue(Issue issue) throws Exception {
        Map<String, Object> issueMap = new HashMap<String, Object>();
        issueMap.put("bID", issue.getId());
        namedParameterJdbcTemplate.update("DELETE FROM tbl_IssueTracker WHERE bID = :bID;", issueMap);
   }

    public List<Issue> getIssues(IssueFilter filter, int page, int numberPerPage) {
        if (filter == null) {
            filter = new IssueFilter();
        }

        List<String> sqlQueries = new ArrayList<String>();
        List<Object> params = new ArrayList<Object>();

        // normal sql query without any filter options
        sqlQueries.add("SELECT " +
                "   * " +
                "FROM " +
                "   tbl_IssueTracker");

        // if there are search queries then build the where
        if (filter.hasSearchCriteria()) {
            sqlQueries.add(buildWhereQuery(filter.getSearchFields(), true, params));
        }

        // if the filter has a sort then order by it
        if (filter.hasSortFilter()) {
            sqlQueries.add(buildOrderQuery(filter.getSortField(), filter.isReverse()));
        }

        // if a range has been set limit the results
        sqlQueries.add(buildLimitQuery(page, numberPerPage, params));

        // combine the statement and return result
        return jdbcTemplate.query(StringUtils.join(sqlQueries.toArray(), " "), params.toArray(),
                new IssueRowMapper());
    }


    private class IssueRowMapper implements RowMapper<Issue> {
        public Issue mapRow(ResultSet resultSet, int i) throws SQLException {
            Issue issue = new Issue();
            issue.setId(resultSet.getLong("bID"));
            issue.setType(IssueType.getIssueType(resultSet.getString("bType")));
            issue.setPage(resultSet.getString("bPage"));
            issue.setDescription(resultSet.getString("bDesc"));
            issue.setComments(resultSet.getString("bComment"));
            issue.setDateLogged(resultSet.getDate("bDateLogged"));
            issue.setDateResolved(resultSet.getDate("bDateResolved"));
            issue.setUpdated(resultSet.getDate("bUpdated"));
            issue.setPriority(IssuePriority.getIssuePriority(resultSet.getString("bPriority")));
            issue.setStatus(IssueStatus.getIssueStatus(resultSet.getString("bStatus")));
            return issue;
        }
    }

}
