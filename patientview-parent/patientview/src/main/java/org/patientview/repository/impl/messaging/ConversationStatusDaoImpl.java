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

package org.patientview.repository.impl.messaging;

import org.patientview.patientview.model.ConversationStatus;
import org.patientview.patientview.model.ConversationStatus_;
import org.patientview.repository.AbstractHibernateDAO;
import org.patientview.repository.messaging.ConversationStatusDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Transactional(propagation = Propagation.MANDATORY)
@Repository(value = "conversationStatusDao")
public class ConversationStatusDaoImpl extends AbstractHibernateDAO<ConversationStatus>
        implements ConversationStatusDao {

    @Override
    public ConversationStatus get(Long id) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ConversationStatus> criteria = builder.createQuery(ConversationStatus.class);
        Root<ConversationStatus> root = criteria.from(ConversationStatus.class);
        List<Predicate> wherePredicates = new ArrayList<Predicate>();
        wherePredicates.add(builder.equal(root.get(ConversationStatus_.id), id));
        buildWhereClause(criteria, wherePredicates);

        try {
            return getEntityManager().createQuery(criteria).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
