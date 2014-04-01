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

package org.patientview.quartz;

import org.apache.commons.lang.ArrayUtils;
import org.patientview.patientview.FindXmlFiles;
import org.patientview.quartz.exception.ProcessException;
import org.patientview.service.ImportManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;

/**
 * Quartz XmlImportTask Job
 */
public class XmlImportTask {

    private static final String LINE_SEPARATOR = System.getProperty("file.separator");

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlImportTask.class);

    @Inject
    private ImportManager importManager;

    @Value("${run.xml.import}")
    private String runXMLImport;

    @Value("${xml.directory}")
    private String xmlDirectory;

    @Value("${xml.patient.data.load.directory}")
    private String xmlPatientDataLoadDirectory;

    private String[] fileEndings = {".xml", };



    @PostConstruct
    public void init() {
        LOGGER.debug("Using {} as a line separator", LINE_SEPARATOR);
        LOGGER.info("Processing from directory {}.", xmlDirectory);
        LOGGER.info("Data loading from directory {}.", xmlDirectory);
    }

    public void execute() {
        LOGGER.info("Started file processing");

        int processed = 0;
        int failed = 0;
        int succeeded = 0;

        if (runImport()) {

            File[] xmlFiles = FindXmlFiles.findXmlFiles(xmlDirectory, fileEndings);

            if (!ArrayUtils.isEmpty(xmlFiles)) {

                for (File xmlFile : xmlFiles) {
                    try {
                        importManager.process(xmlFile);
                        succeeded++;
                    } catch (ProcessException pe) {
                        LOGGER.error("{} file failed to import. Reason: {}.", xmlFile.getName(),  pe.getMessage());
                        if (LOGGER.isDebugEnabled()) {
                            pe.printStackTrace();
                        }
                        failed++;
                    }  finally {
                        processed++;
                        archiveFile(xmlFile);
                    }

                }
            }
        }

        LOGGER.info("Total files processed {}. Total files succeeded {}. Total files failed {}", processed, succeeded,
                failed);
    }

    public void archiveFile(File xmlFile) {

        String unitDirectoryName = xmlPatientDataLoadDirectory + LINE_SEPARATOR + getUnitCode(xmlFile.getName());

        File unitDirectory = new File(unitDirectoryName);

        if (!unitDirectory.exists()) {
            LOGGER.info("Creating subdirectory " + unitDirectoryName);
            if (!unitDirectory.mkdir()) {
                LOGGER.error("Failed to create subdirectory" + unitDirectoryName);
            }
        }

        if (!xmlFile.renameTo(new File(unitDirectoryName, xmlFile.getName()))) {
            LOGGER.error("Unable to archive file after import, deleting instead: {}", xmlFile.getName());

            if (!xmlFile.delete()) {
                LOGGER.error("Unable to delete file after failed archive: {}", xmlFile.getName());
            }

        }
    }

    private String getUnitCode(String filename) {

        String[] filenameParts = filename.split("_");
        if (filenameParts.length > 1) {
            return filenameParts[0];
        } else {
            LOGGER.error("Cannot define unit code from filename so using the 'unknown' folder");
            return "unknown";
        }

    }


    public boolean runImport() {
        return ((runXMLImport != null) && !"false".equals(runXMLImport));
    }

    public String getRunXMLImport() {
        return runXMLImport;
    }

    public void setRunXMLImport(String runXMLImport) {
        this.runXMLImport = runXMLImport;
    }

    public String getXmlDirectory() {
        return xmlDirectory;
    }

    public void setXmlDirectory(String xmlDirectory) {
        this.xmlDirectory = xmlDirectory;
    }

    public void setXmlPatientDataLoadDirectory(final String xmlPatientDataLoadDirectory) {
        this.xmlPatientDataLoadDirectory = xmlPatientDataLoadDirectory;
    }
}
