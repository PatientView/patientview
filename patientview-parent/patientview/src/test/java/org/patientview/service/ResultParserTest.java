package org.patientview.service;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.patientview.patientview.parser.ResultParser;
import org.patientview.quartz.exception.ResultParserException;

import java.io.File;

/**
 * Created by james@solidstatgroup.com on 04/04/2014.
 */
public class ResultParserTest {

    /**
     * Test: To import the diabetes file and get the diabetes specific results.
     *
     */
    @Test
    public void testDiabetesImportFile() {

        File testXml = new File(this.getClass().getClassLoader().getResource("01G_126782_8888888888.xml").getFile());

        try {
            ResultParser resultParser = new ResultParser(testXml);
            resultParser.parse();
            Assert.assertTrue("There should be foot details from this file", CollectionUtils.isNotEmpty(resultParser.getFootCheckupses()));
            Assert.assertTrue("There should be eye details from this file",  CollectionUtils.isNotEmpty(resultParser.getEyeCheckupses()));
        } catch (ResultParserException e) {
            Assert.fail("There should not be an error with this file");
        }

    }


}
