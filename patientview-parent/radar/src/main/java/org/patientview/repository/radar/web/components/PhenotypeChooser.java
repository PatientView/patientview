package org.patientview.repository.radar.web.components;

import org.patientview.model.radar.Phenotype;
import org.patientview.repository.radar.service.ClinicalDataManager;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class PhenotypeChooser extends DropDownChoice<Phenotype> {

    @SpringBean
    private ClinicalDataManager clinicalDataManager;

    public PhenotypeChooser(String id) {
        super(id);
        setChoices(clinicalDataManager.getPhenotypes());
        setChoiceRenderer(new ChoiceRenderer<Phenotype>("description", "id"));
    }
}
