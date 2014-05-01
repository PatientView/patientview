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

package org.patientview.patientview.model;

import org.patientview.ibd.Ibd;
import org.patientview.model.BaseModel;
import org.patientview.patientview.model.enums.SharedThoughtAuditAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "sharedthought_audit")
public class SharedThoughtAudit extends BaseModel {

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sharedthought_id")
    private SharedThought sharedThought;

    @ManyToOne(optional = true)
    @JoinColumn(name = "message_id")
    private Message message;

    @ManyToOne(optional = true)
    @JoinColumn(name = "responder_id")
    private User responder;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SharedThoughtAuditAction action;

    public SharedThoughtAudit() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SharedThought getSharedThought() {
        return sharedThought;
    }

    public void setSharedThought(SharedThought sharedThought) {
        this.sharedThought = sharedThought;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public User getResponder() {
        return responder;
    }

    public void setResponder(User responder) {
        this.responder = responder;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public SharedThoughtAuditAction getAction() {
        return action;
    }

    public void setAction(SharedThoughtAuditAction action) {
        this.action = action;
    }

    public String getDateFormatted() {
        return getDateFormattedDateTime(date);
    }

    private String getDateFormattedDateTime(Date date) {
        if (date != null) {
            return Ibd.DATE_TIME_FORMAT.format(date);
        }

        return "";
    }
}
