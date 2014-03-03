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

package org.patientview.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Integer.parseInt;

/**
 * User: james@solidstategroup.com
 * Date: 12/12/13
 * Time: 11:02
 */
public final class CommonUtils {

    public static final String UK_DATE_FORMAT = "dd-MM-yyyy";

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

    private static final int NHS_NUMBER_LENGTH = 10;
    private static final int NHS_NUMBER_MODULUS = 11;
    private static final int NHS_NUMBER_MODULUS_OFFSET = 11;

    private static final String DATE_FORMAT_0 = "dd-MM-yy";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String[] LENGTH_10_DATE_FORMATS = new String[] {DATE_FORMAT};
    private static final String DATE_FORMAT_1 = "dd.MM.y";
    private static final String DATE_FORMAT_2 = "dd-MM-y";
    private static final String DATE_FORMAT_3 = "dd/MM/y";
    private static final String[] LENGTH_8_DATE_FORMATS = new String[]{DATE_FORMAT_0, DATE_FORMAT_1, DATE_FORMAT_2,
            DATE_FORMAT_3};
    private static final int LENGTH_OF_RADAR_DATES = 8;



    private static final SimpleDateFormat UK_DATE_FORMATTER = new SimpleDateFormat(UK_DATE_FORMAT);

    private CommonUtils() {

    }

    /**
     * Class to return the date from the database text field representation of a date.
     *
     *
     * @param dateField
     * @return
     */
    public static Date parseDate(String dateField) {

        if (StringUtils.hasText(dateField)) {

            // select the dat mask of the length of the field
            String[] dataFormats;
            if (dateField.length() == LENGTH_OF_RADAR_DATES) {
                dataFormats = LENGTH_8_DATE_FORMATS;
            } else {
                dataFormats = LENGTH_10_DATE_FORMATS;
            }

            Date dateOfBirth = null;
            // It seems that the strings in the DB have different date formats, nice.
            for (String dateFormat : dataFormats) {
                try {
                    dateOfBirth = new SimpleDateFormat(dateFormat).parse(dateField);
                    break;
                } catch (ParseException e) {
                    LOGGER.debug("Could not parse date of birth {}", dateField);
                }
            }

            return dateOfBirth;
        }

        return null;
    }

    public static String formatDate(Date date) {
        if (date != null) {
            return UK_DATE_FORMATTER.format(date);
        } else {
            return null;
        }
    }

    /**
     * Uses 5 step NHS number validation, ignoring numeric-only and checksum validation if pseudo NHS numbers allowed
     * Matches order of validation steps from Trello #403, note: when storing NHS number, spaces must be removed
     * @param nhsNumber NHS number, either real or pseudo
     * @param ignoreUppercaseLetters To allow pseudo NHS numbers to be checked if true
     * @return
     */
    public static boolean isNhsNumberValid(String nhsNumber, boolean ignoreUppercaseLetters) {
        // 1. Any spaces should be removed from the value
        nhsNumber = nhsNumber.replaceAll("\\s", "");

        if (!ignoreUppercaseLetters) {
            // 2. (non-pseudo only) The value should contain only numbers if this isn't the case it should be rejected.
            if (!nhsNumber.matches("[0-9]+")) {
                return false;
            }

            // 3. (non-pseudo only) If the length of the value is 9 characters a 0 should be prepended to the value.
            // This is due to some CHI (Scotland) numbers starting with 0 which is lost if the value is handled
            // as a number.
            if (nhsNumber.length() == NHS_NUMBER_LENGTH - 1) {
                nhsNumber = "0" + nhsNumber;
            }
        }

        // 4. If the value is not now 10 characters it should be rejected.
        if (nhsNumber.length() != NHS_NUMBER_LENGTH) {
            return false;
        }

        // 5. The NHS Check Digit function should be run on the value. If this fails it should be rejected.
        // note: checksum ignored if ignoreUppercaseLetters is true
        return (ignoreUppercaseLetters ? true : isNhsChecksumValid(nhsNumber));
    }


    public static boolean isNhsNumberValid(String nhsNumber) {
        return CommonUtils.isNhsNumberValid(nhsNumber, false);
    }

    public static boolean isNhsNumberValidWhenUppercaseLettersAreAllowed(String nhsNumber) {
        return CommonUtils.isNhsNumberValid(nhsNumber, true);
    }

    private static boolean isNhsChecksumValid(String nhsNumber) {
        /**
         * Generate the checksum using modulus 11 algorithm
         */
        int checksum = 0;

        try {
            // Multiply each of the first 9 digits by 10-character position (where the left character is in position 0)
            for (int i = 0; i < NHS_NUMBER_LENGTH - 1; i++) {
                int value = parseInt(nhsNumber.charAt(i) + "") * (NHS_NUMBER_LENGTH - i);
                checksum += value;
            }

            //(modulus 11)
            checksum = NHS_NUMBER_MODULUS_OFFSET - checksum % NHS_NUMBER_MODULUS;

            if (checksum == NHS_NUMBER_MODULUS_OFFSET) {
                checksum = 0;
            }

            // Does checksum match the 10th digit?
            return checksum == parseInt(String.valueOf(nhsNumber.charAt(NHS_NUMBER_LENGTH - 1)));


        } catch (NumberFormatException e) {
            return false; // nhsNumber contains letters
        }
    }
}
