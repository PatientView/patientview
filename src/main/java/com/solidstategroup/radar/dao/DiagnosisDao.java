package com.solidstategroup.radar.dao;

import com.solidstategroup.radar.model.ClinicalPresentation;
import com.solidstategroup.radar.model.Diagnosis;
import com.solidstategroup.radar.model.DiagnosisCode;
import com.solidstategroup.radar.model.Karotype;

import java.util.List;

public interface DiagnosisDao {

    Diagnosis getDiagnosis(long id);

    DiagnosisCode getDiagnosisCode(long id);

    List<DiagnosisCode> getDiagnosisCodes();

    ClinicalPresentation getClinicalPresentation(long id);

    List<ClinicalPresentation> getClinicalPresentations();

    Karotype getKarotype(long id);

    List<Karotype> getKarotypes();
}
