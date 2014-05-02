package org.patientview.test.quartz;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.patientview.patientview.XmlImportUtils;
import org.patientview.quartz.exception.PatientNotMappedException;
import org.patientview.quartz.handler.ErrorHandler;
import org.patientview.quartz.handler.impl.ErrorHandlerImpl;
import org.patientview.service.LogEntryManager;
import org.patientview.service.impl.LogEntryManagerImpl;

import java.io.File;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by james@solidstategroup.com on 02/05/2014.
 */
public class ErrorHandlerTest {

    @Mock
    private XmlImportUtils xmlImportUtils = new XmlImportUtils();

    @Mock
    private LogEntryManager logEntryManager = new LogEntryManagerImpl();

    @InjectMocks
    private ErrorHandler errorHandler = new ErrorHandlerImpl();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }


    /**
     * Test: To make sure the correct email method gets sent for a PatientNotMappedException exception
     * Fail: The correct method will not be called.
     */
    @Test
    public void testForPatientNotMappingError() {
        File file = new File("A_122345_1111111111.xml");
        PatientNotMappedException patientNotMappedException = new PatientNotMappedException("This is a test");

        errorHandler.processingException(file, patientNotMappedException);

        verify(xmlImportUtils, Mockito.times(1)).sendEmailOfUnmappingPatientUnitAdmin(
                any(PatientNotMappedException.class), any(File.class));

    }


}
