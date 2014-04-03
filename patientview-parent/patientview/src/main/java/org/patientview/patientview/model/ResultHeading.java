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
import org.patientview.model.Specialty;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Set;

@Entity
@Table(name = "result_heading")
public class ResultHeading extends BaseModel {

    @Column(nullable = false, unique = true)
    private String headingcode;

    @Column(nullable = false)
    private String heading;

    @Column(nullable = false)
    private String rollover;

    @Column(nullable = false)
    private String link;

    @Transient
    private int panel;

    @Transient
    private int panelorder;

    @Column(nullable = true)
    private Double minRangeValue;

    @Column(nullable = true)
    private Double maxRangeValue;

    @Column(nullable = true)
    private String units;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "resultHeading", cascade = CascadeType.ALL)
    private Set<SpecialtyResultHeading> specialtyResultHeadings;

    public ResultHeading() {
    }

    public ResultHeading(String headingcode) {
        this.headingcode = headingcode;
    }

    public ResultHeading(String heading, Specialty specialty, String rollover, String headingcode, String link,
                         int panel, int panelorder) {
        this.heading = heading;
        this.rollover = rollover;
        this.headingcode = headingcode;
        this.link = link;
        this.panel = panel;
        this.panelorder = panelorder;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getRollover() {
        return rollover;
    }

    public void setRollover(String rollover) {
        this.rollover = rollover;
    }

    public String getHeadingcode() {
        return headingcode;
    }

    public void setHeadingcode(String headingcode) {
        this.headingcode = headingcode;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getPanel() {
        return panel;
    }

    public void setPanel(int panel) {
        this.panel = panel;
    }

    public int getPanelorder() {
        return panelorder;
    }

    public void setPanelorder(int panelorder) {
        this.panelorder = panelorder;
    }

    public Double getMinRangeValue() { return minRangeValue; }

    public void setMinRangeValue(Double minRangeValue) { this.minRangeValue = minRangeValue; }

    public Double getMaxRangeValue() { return maxRangeValue; }

    public void setMaxRangeValue(Double maxRangeValue) { this.maxRangeValue = maxRangeValue; }

    public String getUnits() { return units; }

    public void setUnits(String units) { this.units = units; }


    public Set<SpecialtyResultHeading> getSpecialtyResultHeadings() {
        return specialtyResultHeadings;
    }

    public void setSpecialtyResultHeadings(final Set<SpecialtyResultHeading> specialtyResultHeadings) {
        this.specialtyResultHeadings = specialtyResultHeadings;
    }
}
