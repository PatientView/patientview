package org.patientview.service;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.patientview.model.Specialty;
import org.patientview.patientview.model.ResultHeading;
import org.patientview.patientview.model.SpecialtyResultHeading;
import org.patientview.repository.ResultHeadingDao;
import org.patientview.service.impl.ResultHeadingManagerImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by jameseaton@solidstategroup.com on 02/04/2014.
 */
public class ResultHeadingManagerTest {

    private Specialty specialty;

    @Mock
    private ResultHeadingDao resultHeadingDao;

    @Mock
    private SecurityUserManager securityUserManager;

    @InjectMocks
    private ResultHeadingManager resultHeadingManager = new ResultHeadingManagerImpl();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        specialty = createSpecialty(1);
        when(securityUserManager.getLoggedInSpecialty()).thenReturn(specialty);
    }


    @Test
    public void getAllBySpecialtyTestSingleResult() {

        final String specialtyRollover = "specialtyRollover";
        final String specialtyHeading = "testHeading";
        final String globalHeading = "globalHeading";
        final String globalRollover = "globalRollover";

        //Create the list of heading for the DAO
        List<ResultHeading> resultHeadings = new ArrayList<ResultHeading>();
        ResultHeading resultHeading = createGlobalResultHeading(globalHeading, globalRollover);

        Set<SpecialtyResultHeading> specialtyResultHeadings = new HashSet<SpecialtyResultHeading>();
        specialtyResultHeadings.add(createSpecialtyResultHeading(specialty, specialtyHeading, specialtyRollover));
        resultHeading.setSpecialtyResultHeadings(specialtyResultHeadings);
        resultHeadings.add(resultHeading);


        when(resultHeadingDao.getAll(any(Specialty.class))).thenReturn(resultHeadings);

        resultHeadings = resultHeadingDao.getAll(specialty);

        Assert.assertTrue(CollectionUtils.isNotEmpty(resultHeadings));
        Assert.assertTrue(resultHeadings.size() == 1);
        resultHeading = resultHeadings.get(0);
        Assert.assertTrue(CollectionUtils.isNotEmpty(resultHeading.getSpecialtyResultHeadings()));

    }

    @Test
    public void getAllBySpecialtyTestSingleMultipleResults() {

        final String specialtyRollover = "specialtyRollover";
        final String specialtyHeading = "testHeading";
        final String globalHeading = "globalHeading";
        final String globalRollover = "globalRollover";

        //Create the list of heading for the DAO
        List<ResultHeading> resultHeadings = new ArrayList<ResultHeading>();
        ResultHeading resultHeading = createGlobalResultHeading(globalHeading, globalRollover);

        Set<SpecialtyResultHeading> specialtyResultHeadings = new HashSet<SpecialtyResultHeading>();
        specialtyResultHeadings.add(createSpecialtyResultHeading(specialty, specialtyHeading, specialtyRollover));

        // Add another heading setting from another specialty
        specialtyResultHeadings.add(createSpecialtyResultHeading(createSpecialty(2), specialtyHeading, specialtyRollover));
        resultHeading.setSpecialtyResultHeadings(specialtyResultHeadings);
        resultHeadings.add(resultHeading);


        when(resultHeadingDao.getAll(any(Specialty.class))).thenReturn(resultHeadings);

        resultHeadings = resultHeadingDao.getAll(specialty);

        Assert.assertTrue(CollectionUtils.isNotEmpty(resultHeadings));
        Assert.assertTrue(resultHeadings.size() == 1);
        resultHeading = resultHeadings.get(0);
        Assert.assertTrue(CollectionUtils.isNotEmpty(resultHeading.getSpecialtyResultHeadings()));

    }


    private ResultHeading createGlobalResultHeading(String heading, String rollover) {
        ResultHeading resultHeading = new ResultHeading();
        resultHeading.setHeading(heading);
        resultHeading.setRollover(rollover);
        return resultHeading;
    }

    private SpecialtyResultHeading createSpecialtyResultHeading(Specialty specialty, String heading, String rollover) {
        SpecialtyResultHeading specialtyResultHeading = new SpecialtyResultHeading();
        specialtyResultHeading.setHeading(heading);
        specialtyResultHeading.setRollover(rollover);
        specialtyResultHeading.setSpecialtyId(specialty.getId().intValue());
        return specialtyResultHeading;
    }

    private Specialty createSpecialty(long id) {
        Specialty specialty = new Specialty();
        specialty.setName("Test Speciality");
        specialty.setId(id);
        return specialty;

    }



}
