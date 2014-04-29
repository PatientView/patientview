package org.patientview.test.importer;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.patientview.model.Patient;
import org.patientview.model.Specialty;
import org.patientview.model.Unit;
import org.patientview.patientview.model.UserMapping;
import org.patientview.quartz.exception.ProcessException;
import org.patientview.quartz.handler.ErrorHandler;
import org.patientview.quartz.handler.impl.ErrorHandlerImpl;
import org.patientview.repository.UnitDao;
import org.patientview.repository.UserMappingDao;
import org.patientview.repository.impl.UnitDaoImpl;
import org.patientview.repository.impl.UserMappingDaoImpl;
import org.patientview.service.DiagnosisManager;
import org.patientview.service.DiagnosticManager;
import org.patientview.service.ImportManager;
import org.patientview.service.LetterManager;
import org.patientview.service.MedicineManager;
import org.patientview.service.PatientManager;
import org.patientview.service.TestResultManager;
import org.patientview.service.UnitManager;
import org.patientview.service.ibd.IbdManager;
import org.patientview.service.ibd.impl.IbdManagerImpl;
import org.patientview.service.impl.DiagnosisManagerImpl;
import org.patientview.service.impl.DiagnosticManagerImpl;
import org.patientview.service.impl.ImportManagerImpl;
import org.patientview.service.impl.LetterManagerImpl;
import org.patientview.service.impl.MedicineManagerImpl;
import org.patientview.service.impl.PatientManagerImpl;
import org.patientview.service.impl.TestResultManagerImpl;
import org.patientview.service.impl.UnitManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This principal of this to capture a couple of high level functions of the current ImportManager.
 * As well as this we want to do it without the DB integration.
 *
 *
 * Created by james@solidstategroup.com  on 31/03/2014.
 */
@RunWith(MockitoJUnitRunner.class)
public class ImporterMockTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(ImporterMockTest.class);


    @InjectMocks
    private ImportManager importManager = new ImportManagerImpl();

    @Mock
    private UnitManager unitManager = new UnitManagerImpl();

    @Mock
    private UnitDao unitDao = new UnitDaoImpl();

    @Mock
    private PatientManager patientManager = new PatientManagerImpl();

    @Mock
    private UserMappingDao userMappingDao = new UserMappingDaoImpl();

    @Mock
    private TestResultManager testResultManager = new TestResultManagerImpl();

    @Mock
    private LetterManager letterManager = new LetterManagerImpl();

    @Mock
    private DiagnosisManager diagnosisManager = new DiagnosisManagerImpl();

    @Mock
    private DiagnosticManager diagnosticManager = new DiagnosticManagerImpl();

    @Mock
    private MedicineManager medicineManager = new MedicineManagerImpl();

    @Mock
    private IbdManager ibdManager = new IbdManagerImpl();

    @Mock
    private ErrorHandler errorHandler = new ErrorHandlerImpl();


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }


    /**
     * Test: To run the test with a valid XML file and the process does not fail.
     *       Does to importer try and save the patient
     * Fail: Any processing error
     *
     */
    @Test
    public void testProcess() throws IOException {

        File testXml = getFile("A_00794_1234567890.gpg.xml");


        when(unitDao.get(anyString(), any(Specialty.class))).thenReturn(getCorrectUnit());
        when(userMappingDao.getAllByNhsNo(anyString())).thenReturn(getCorrectMappings());

        try {
            importManager.process(testXml);
            verify(patientManager, Mockito.times(1)).save(any(Patient.class));
            LOGGER.info("Successfully completed processing patient");
        } catch (ProcessException pe) {
            LOGGER.error(pe.getMessage());
            Assert.fail("This file should not fail");

        }
        testXml.delete();
    }

    /**
     * Test: To check a file with an invalid unit code fails the test
     *       The importer does not try and save the patient
     * Fail: The test does not throw a ProcessException
     */
    @Test
    public void testProcessWithInvalidUnitCode() {
        File testXml = getFile("A_00794_1234567890-InvalidUnitCode.gpg.xml");

        when(unitDao.get(eq("XXXX"), any(Specialty.class))).thenReturn(null);
        when(userMappingDao.getAllByNhsNo(anyString(), anyString())).thenReturn(getCorrectMappings());

        try {
            importManager.process(testXml);
            Assert.fail("The process should have not completed successfully");
        } catch (ProcessException pe) {
            verify(patientManager, Mockito.times(0)).save(any(Patient.class));
            LOGGER.info(pe.getMessage());
        }

    }

    /**
     * Test: To check a file with an invalid nhs number
     *       The importer does not try and save the patient
     * Fail: The test does not throw a ProcessException
     *
     */
    @Test
    public void testProcessWithInvalidNhsNumber() {
        File testXml = getFile("A_00794_1234567890-InvalidNHSNumber.gpg.xml");

        when(unitDao.get(anyString(), any(Specialty.class))).thenReturn(getCorrectUnit());
        when(userMappingDao.getAllByNhsNo(anyString(), anyString())).thenReturn(getCorrectMappings());

        try {
            importManager.process(testXml);
            Assert.fail("The process should have not completed successfully");
        } catch (ProcessException pe) {
            verify(patientManager, Mockito.times(0)).save(any(Patient.class));
            LOGGER.info(pe.getMessage());
        }

    }

    /**
     * Test: To check a file containing a patient not already in a unit does not process
     *       The importer does not try and save the patient
     * Fail: The test does not throw a ProcessException
     */
    @Test
    public void testProcessWithThePatientNotInTheUnit() {

        File testXml = getFile("A_00794_1234567890.gpg.xml");

        when(unitDao.get(anyString(), any(Specialty.class))).thenReturn(getCorrectUnit());
        when(userMappingDao.getAllByNhsNo(anyString())).thenReturn(null);

        try {
            importManager.process(testXml);
            Assert.fail("This process should not complete with no user mappings returned for the patient");
        } catch (ProcessException pe) {
            verify(patientManager, Mockito.times(0)).save(any(Patient.class));
            LOGGER.info(pe.getMessage());
        }


    }


    /**
     * Test: To check a file containing a patient not already in a unit does not process
     *       The importer does not try and save the patient
     * Fail: The test does not throw a ProcessException
     */
    @Test
    public void testProcessWithThePatientNotInCorrectUnit() {

        File testXml = getFile("A_00794_1234567890.gpg.xml");
        when(unitDao.get(anyString(), any(Specialty.class))).thenReturn(getCorrectUnit());
        when(userMappingDao.getAllByNhsNo(anyString())).thenReturn(getWrongMappings());

        try {
            importManager.process(testXml);
            Assert.fail("This process should not complete with incorrect mappings returned for the patient");
        } catch (ProcessException pe) {
            verify(patientManager, Mockito.times(0)).save(any(Patient.class));
            LOGGER.info(pe.getMessage());
        }
    }

    /**
     * Test: Trello 495 validate a NHS No against a Radar patient that has a mapping to a RadarGroup.
     * Fail: An exception is not thrown for a patient with a Radar mapping.
     *
     */
    @Test
    public void testProcessWithPatientInARadarUnit() {
        File testXml = getFile("A_00794_1234567890.gpg.xml");

        // Trigger the check for Radar
        when(unitDao.get(anyString(), any(Specialty.class))).thenReturn(getCorrectUnit());
        when(userMappingDao.getAllByNhsNo(anyString())).thenReturn(getRadarMappings());

        when(unitDao.getAll(null, new String[]{"radargroup"})).thenReturn(getRadarUnits());
        try {
            importManager.process(testXml);
            verify(patientManager, Mockito.times(1)).save(any(Patient.class));;
        } catch (ProcessException pe) {
            Assert.fail("This process should not raise an exception for a radar patient with mapping");
            LOGGER.info(pe.getMessage());
        }

    }

    /**
     * Test: Trello 495 validate a NHS No against a Radar patient that has a mapping to a RadarGroup.
     * Fail: An exception is thrown for a patient without a Radar mapping or any other valid unit mapping.
     *
     */
    @Test
    public void testProcessWithPatientNotInARadarUnitOrOtherUnit() {
        File testXml = getFile("A_00794_1234567890.gpg.xml");

        when(unitDao.get(anyString(), any(Specialty.class))).thenReturn(getCorrectUnit());
        when(userMappingDao.getAllByNhsNo(anyString())).thenReturn(getWrongMappings());
        when(unitDao.getAll(null, new String[]{"radargroup"})).thenReturn(getRadarUnits());
        try {
            importManager.process(testXml);
            Assert.fail("This process should not raise an exception for a radar patient with mapping");
        } catch (ProcessException pe) {
            LOGGER.info(pe.getMessage());
        }

    }

    private File getFile(String filename) {
        File testXml = new File("temp");
        try {
            FileUtils.copyInputStreamToFile(
                    this.getClass().getClassLoader().getResourceAsStream(filename), testXml);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return testXml;
    }

    private Unit getCorrectUnit() {
        Unit unit = new Unit();
        unit.setUnitcode("A");
        return unit;

    }

    private List<UserMapping> getCorrectMappings() {

        List<UserMapping> correctUserMappings = new ArrayList<UserMapping>();
        UserMapping userMapping = new UserMapping();
        userMapping.setUnitcode("A");

        correctUserMappings.add(userMapping);

        return correctUserMappings;
    }

    private List<UserMapping> getWrongMappings() {

        List<UserMapping> wrongUserMappings = new ArrayList<UserMapping>();
        UserMapping userMapping = new UserMapping();
        userMapping.setUnitcode("B");

        wrongUserMappings.add(userMapping);

        return wrongUserMappings;
    }

    private List<Unit> getRadarUnits() {

        List<Unit> units = new ArrayList<Unit>();
        Unit unit = new Unit();
        unit.setUnitcode("radar");
        units.add(unit);
        return units;
    }

    private List<UserMapping> getRadarMappings() {

        List<UserMapping> radarUserMappings = new ArrayList<UserMapping>();
        UserMapping userMapping = new UserMapping();
        userMapping.setUnitcode("radar");
        radarUserMappings.add(userMapping);
        return radarUserMappings;
    }
}
