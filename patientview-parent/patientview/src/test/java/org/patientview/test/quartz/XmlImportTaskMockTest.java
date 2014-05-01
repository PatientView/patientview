package org.patientview.test.quartz;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.patientview.quartz.XmlImportTask;
import org.patientview.service.ImportManager;
import org.patientview.service.impl.ImportManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * This class in mainly to test Trello 407 Routing Archive Files into Sub Directories
 *
 * Created by james@soldistategroup.com on 31/03/2014.
 */
public class XmlImportTaskMockTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(XmlImportTaskMockTest.class);

    private final static String xmlImportDirectoryName = "testImportDir";
    private final static String xmlArchiveDirectoryName = "testArchiveDir";

    private final static String lineSeparator = System.getProperty("file.separator");

    private static File importDir;
    private static File archiveDir;


    @InjectMocks
    private XmlImportTask xmlImportTask = new XmlImportTask();

    @Mock
    private ImportManager importManager = new ImportManagerImpl();


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);


        importDir = new File(xmlImportDirectoryName);
        archiveDir = new File(xmlArchiveDirectoryName);

        importDir.mkdir();
        archiveDir.mkdir();


    }

    /**
     * Test: The importer is sending files into the archive directory with a directory for each user
     * Fail: One of the created test files does not find it's way into the archive directory.
     *
     */
    @Test
    public void testImportTask() {


        xmlImportTask.setRunXMLImport("true");
        xmlImportTask.setXmlDirectory(xmlImportDirectoryName);
        xmlImportTask.setXmlPatientDataLoadDirectory(xmlArchiveDirectoryName);

        String[] testFiles = createTestFiles();

        xmlImportTask.execute();

        for (String testFile : testFiles) {
            File unitDir = new File(xmlArchiveDirectoryName + lineSeparator + getUnitCode(testFile));
            File archiveXml = new File(unitDir.getPath() + lineSeparator + testFile);
            Assert.assertTrue("Is the archive directory for the unit is not present", unitDir.exists());
            Assert.assertTrue("Is the archive directory is not a directory", unitDir.isDirectory());
            Assert.assertTrue("Is the file is not in the archive directory", archiveXml.exists());
        }

    }

    @After
    public void tearDown() {
        try {
            FileUtils.deleteDirectory(archiveDir);
            FileUtils.deleteDirectory(importDir);
        } catch (IOException io) {
            LOGGER.error("Failed to clean up", io);
        }
    }

    private String[] createTestFiles() {

        String[] testFiles = {"UNIT1_CODE_00000000001.xml","UNIT1_CODE_00000000002.xml", "UNIT2_CODE_00000000002.xml"};

        //Create some blank input files
        for (String filename : testFiles) {
            File file = new File(xmlImportDirectoryName + lineSeparator + filename);
            try {
                file.createNewFile();
            } catch (IOException io) {
                LOGGER.error("Can't create test file", io);
            }
        }

        return testFiles;
    }

    private String getUnitCode(String filename) {

        String[] filenameParts = filename.split("_");
        if (filenameParts.length > 1) {
            return filenameParts[0];
        } else {
            return "unknown";
        }

    }


}
