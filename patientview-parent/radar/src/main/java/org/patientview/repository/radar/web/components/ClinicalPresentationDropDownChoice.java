package org.patientview.repository.radar.web.components;

import org.patientview.model.radar.ClinicalPresentation;
import org.patientview.repository.radar.service.DiagnosisManager;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ClinicalPresentationDropDownChoice extends DropDownChoice<ClinicalPresentation> {

    @SpringBean
    private DiagnosisManager diagnosisManager;

    public ClinicalPresentationDropDownChoice(String id) {
        super(id);
        setChoices(diagnosisManager.getClinicalPresentations());
        setChoiceRenderer(new ChoiceRenderer<ClinicalPresentation>("name", "id"));
    }
}
