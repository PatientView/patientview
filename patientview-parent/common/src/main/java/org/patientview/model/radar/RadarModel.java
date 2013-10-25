package org.patientview.model.radar;

/**
 * Basic model to be extended which includes a radar number field.
 */
public abstract class RadarModel extends BaseModel {

    private Long radarNumber;

    public Long getRadarNumber() {
        return radarNumber;
    }

    public void setRadarNumber(Long radarNumber) {
        this.radarNumber = radarNumber;
    }
}
