package org.patientview.service;

import org.junit.Assert;
import org.junit.Test;
import org.patientview.patientview.parser.ResultParser;
import org.patientview.quartz.exception.ResultParserException;

import java.io.File;

/**
 * Created by james@solidstatgroup.com on 04/04/2014.
 */
public class ResultParserTest {

    /***
     * Test: To import the
     *
     */
    @Test
    public void testDiabatesImportFile() {

        File testXml = new File(this.getClass().getClassLoader().getResource("DIA01_000555_9999999999.xml").getFile());

        try {
            ResultParser resultParser = new ResultParser(testXml);
            resultParser.parse();
            Assert.assertNotNull("There should be foot details from this file", resultParser.getFootCheckupses());
            Assert.assertNotNull("There should be eye details from this file", resultParser.getEyeCheckupses());
        } catch (ResultParserException e) {
            Assert.fail("There should not be an error with this file");
        }

    }


}
