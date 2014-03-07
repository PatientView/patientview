package org.patientview.radar.util;

import org.apache.wicket.util.convert.converter.DateConverter;
import org.apache.wicket.util.string.Strings;
import org.patientview.util.CommonUtils;

import java.util.Date;
import java.util.Locale;

/**
 * Created by jamesr on 07/03/2014.
 */
public class RadarDateConverter extends DateConverter {
    private static final long serialVersionUID = 1L;

    @Override
    public Date convertToObject(final String value, final Locale locale) {
        if ((value == null) || Strings.isEmpty(value)) {
            return null;
        } else {
            return CommonUtils.parseUKDate(value);
        }
    }
}