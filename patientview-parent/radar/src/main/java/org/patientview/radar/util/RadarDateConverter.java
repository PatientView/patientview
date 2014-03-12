package org.patientview.radar.util;

import org.apache.wicket.util.convert.converter.DateConverter;
import org.apache.wicket.util.string.Strings;
import org.patientview.util.CommonUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jamesr on 07/03/2014.
 */
public class RadarDateConverter extends DateConverter {
    private static final long serialVersionUID = 1L;

    /**
     * Enforce strict date parsing using CommonUtils
     * @param value Date string to convert to Date object
     * @param locale
     * @return Output of CommonUtils.parseUKDate(value);
     */
    @Override
    public Date convertToObject(final String value, final Locale locale) {
        if ((value == null) || Strings.isEmpty(value)) {
            return null;
        } else {
            return CommonUtils.parseUKDate(value);
        }
    }

    /**
     * Enforce standard conversion of dates to RADAR dd-MM-yyyy format (not default en_US format of M/d/yy)
     * @param value
     * @param locale
     * @return
     */
    @Override
    public String convertToString(final Date value, final Locale locale) {
        return new SimpleDateFormat("dd-MM-yyyy").format(value);
    }
}