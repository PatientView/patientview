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

package org.patientview.test.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.patientview.util.CommonUtils;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class CommonUtilsTest {

    @Test
    public void testNHSNoMissingDigits() {
        assertTrue("Valid 9 digit NHS No did not pass validation", CommonUtils.isNhsNumberValid("012000299"));
    }

    @Test
    public void testNHSNoExtraDigits() {
        assertFalse("Invalid NHS No passed validation", CommonUtils.isNhsNumberValid("12345678901"));
    }

    @Test
    public void testBadNHSNoChecksum() {
        assertFalse("Invalid NHS No passed validation", CommonUtils.isNhsNumberValid("7428721471"));
    }

    @Test
    public void testValidNHSNoChecksum() {
        assertTrue("Valid NHS No did not pass validation", CommonUtils.isNhsNumberValid("7428721474"));
    }

    @Test
    public void testLowercaseNHSNo() {
        assertFalse("Invalid NHS No passed validation", CommonUtils.isNhsNumberValid("12f45a6789"));
    }

    @Test
    public void testOverridingValidationWithUppercaseNHSNo() {
        assertTrue("Invalid NHS No with uppercase letters did not pass validation although validation should have " +
                "been overridden", CommonUtils.isNhsNumberValidWhenUppercaseLettersAreAllowed("12F45A6789"));
    }

    @Test
    public void testCleanNHSNo() {
        assertEquals("0123456789", CommonUtils.cleanNhsNumber("123 456 789"));
    }

    @Test
    public void testCleanNHSNoAlphanumeric() {
        assertEquals("0XY3456AB9",CommonUtils.cleanNhsNumber("XY3 456 AB9"));
    }

    @Test
    public void testCleanAndValidateNumericNHSNo() {
        assertTrue("Valid unclean NHS No did not pass validation", CommonUtils.isNhsNumberValid(CommonUtils.cleanNhsNumber("012 000 299")));
    }

    @Test
    public void testCleanAndValidateAlphanumericNHSNo() {
        assertTrue("Valid unclean NHS No did not pass validation", CommonUtils.isNhsNumberValidWhenUppercaseLettersAreAllowed(CommonUtils.cleanNhsNumber("AB 123 456 X")));
    }

    @Test
    public void testIncorrectDateParse(){
        String incorrectDate = "2014-02-99";

        // will return null due to setLenient(false) in parser
        Date date = CommonUtils.parseDate(incorrectDate);
        assertEquals("Incorrect date should return NULL", null, date);
    }

    @Test
    public void testCorrectDateParse(){
        String correctDate = "2014-02-12";

        Date date = CommonUtils.parseDate(correctDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Assert.assertTrue("The month for " + correctDate + " should be 2, is actually " +
                (calendar.get(Calendar.MONTH)+1), (calendar.get(Calendar.MONTH)+1) == 2);
        Assert.assertTrue("The day of the month for " + correctDate + " should be 12, is actually " +
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.DAY_OF_MONTH) == 12);
    }
}
