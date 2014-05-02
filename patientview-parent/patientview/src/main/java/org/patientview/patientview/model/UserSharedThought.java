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

import org.patientview.model.BaseModel;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_sharedthought")
public class UserSharedThought extends BaseModel {

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sharedthought_id")
    private SharedThought sharedThought;

    @Column(name = "viewed")
    private boolean viewed = false;

    public UserSharedThought() {
    }

    public User getUser() {
        return user;
    }
    public void setUser(final User user) {
        this.user = user;
    }

    public SharedThought getSharedThought() {
        return sharedThought;
    }
    public void setSharedThought(SharedThought sharedThought) {
        this.sharedThought = sharedThought;
    }

    public boolean isViewed() {
        return viewed;
    }
    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }
}
