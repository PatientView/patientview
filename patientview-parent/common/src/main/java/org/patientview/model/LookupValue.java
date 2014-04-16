package org.patientview.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by james@solidstategroup.com on 15/04/2014.
 */
@Entity
@Table(name = "lookup_value")
public class LookupValue extends BaseModel {

    @Column(nullable = true, name = "lookup_value")
    private String value;

    @Column(nullable = true, name = "lookup_text")
    private String text;

    @JsonIgnore
    @JoinColumn(name = "lookup_type_id")
    @ManyToOne
    private LookupType lookupType;

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public LookupType getLookupType() {
        return lookupType;
    }

    public void setLookupType(final LookupType lookupType) {
        this.lookupType = lookupType;
    }
}
