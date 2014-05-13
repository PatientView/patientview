package org.patientview.patientview.model;

import org.patientview.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by james@solidstategroup.com on 02/04/2014.
 */
@Entity
@Table(name = "specialty_result_heading")
public class SpecialtyResultHeading extends BaseModel {

    @Column(nullable = true)
    private String heading;

    @Column(nullable = true)
    private String rollover;

    @Column(nullable = true)
    private int panel;

    @Column(nullable = true)
    private int panelOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "result_heading_id")
    private ResultHeading resultHeading;

    @Column(nullable = false, name = "specialty_Id")
    private int specialtyId;


    public String getHeading() {
        return heading;
    }

    public void setHeading(final String heading) {
        this.heading = heading;
    }

    public String getRollover() {
        return rollover;
    }

    public void setRollover(final String rollover) {
        this.rollover = rollover;
    }

    public int getPanel() {
        return panel;
    }

    public void setPanel(final int panel) {
        this.panel = panel;
    }

    public int getPanelOrder() {
        return panelOrder;
    }

    public void setPanelOrder(final int panelOrder) {
        this.panelOrder = panelOrder;
    }

    public ResultHeading getResultHeading() {
        return resultHeading;
    }

    public void setResultHeading(final ResultHeading resultHeading) {
        this.resultHeading = resultHeading;
    }

    public int getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(final int specialtyId) {
        this.specialtyId = specialtyId;
    }
}
