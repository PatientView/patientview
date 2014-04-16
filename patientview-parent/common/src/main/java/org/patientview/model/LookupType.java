package org.patientview.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.Map;

/**
 * Created by james@solidtstategroup.com     on 14/04/2014.
 */
@Entity
@Table(name = "lookup_type")
public class LookupType extends BaseModel {

    @Column(nullable = false, name = "lookup_name")
    private String name;

    @Column(nullable = false, name = "lookup_type")
    private String type;

    @Column(nullable = true, name = "component_name")
    private String componentName;

    @Column(nullable = false, name = "created")
    private Date date;

    @OneToMany(mappedBy = "lookupType", fetch = FetchType.EAGER)
    @MapKeyColumn(name = "LOOKUP_VALUE")
    private Map<String, LookupValue> lookupValues;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(final String componentName) {
        this.componentName = componentName;
    }

    public Map<String, LookupValue> getLookupValues() {
        return lookupValues;
    }

    public void setLookupValues(final Map<String, LookupValue> lookupValues) {
        this.lookupValues = lookupValues;
    }
}
