package org.patientview.radar.web.panels;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.patientview.model.Patient;
import org.patientview.model.generic.DiseaseGroup;
import org.patientview.radar.model.generic.AddPatientModel;
import org.patientview.radar.service.DemographicsManager;
import org.patientview.radar.service.UserManager;
import org.patientview.radar.web.pages.BasePage;
import org.patientview.radar.web.pages.patient.GenericPatientPage;
import org.patientview.radar.web.pages.patient.alport.AlportPatientPage;
import org.patientview.radar.web.pages.patient.hnf1b.HNF1BPatientPage;
import org.patientview.radar.web.pages.patient.srns.SrnsPatientPage;

import java.util.List;

/**
 * User: james@solidstategroup.com
 * Date: 06/11/13
 * Time: 15:40
 */
public class SelectPatientPanel extends Panel {


    @SpringBean
    private DemographicsManager demographicsManager;

    @SpringBean
    private UserManager userManager;

    private IModel<List<Patient>> model;
    private AddPatientModel patientModel;

    public SelectPatientPanel(String id,  IModel<List<Patient>> model) {
        super(id);
        this.model = model;
        init();
    }


    private void init() {

        add(new FeedbackPanel("patientSelection"));
        // Create the form that select patients to link with
        add(createSelectionForm());
        // Add the form that will create a patient from scratch
        add(createCreateForm());

    }


    private BasePage getDiseasePage(DiseaseGroup diseaseGroup, Patient patient){
        if (diseaseGroup != null) {
            if (diseaseGroup.getId().equals(DiseaseGroup.SRNS_DISEASE_GROUP_ID) ||
                    diseaseGroup.getId().
                            equals(DiseaseGroup.MPGN_DISEASEGROUP_ID)) {
                return new SrnsPatientPage(null);
            } else if (diseaseGroup.getId().equals(DiseaseGroup.ALPORT_DISEASEGROUP_ID)) {
                return new AlportPatientPage(patient);
            } else if (diseaseGroup.getId().equals(DiseaseGroup.HNF1B_DISEASEGROUP_ID)) {
                return new HNF1BPatientPage(patient);
            } else {
                return new GenericPatientPage(patient);
            }
        }  else {
            return new GenericPatientPage(patient);
        }

    }

    private Form createSelectionForm() {

        // Form that displays the potential patient records to link with from the Patient table

        IModel<Patient> selected = new Model<Patient>();
        final RadioGroup group = new RadioGroup("group", selected);


        Form<?> form = new Form<Patient>("patientSelectionForm") {
            @Override
            protected void onSubmit() {

                Patient patient = (Patient) group.getDefaultModelObject();
                patient = demographicsManager.get(patient.getId());


                DiseaseGroup diseaseGroup = patientModel.getDiseaseGroup();
                setResponsePage(getDiseasePage(diseaseGroup, patient));

            }
        };

        // submit link
        AjaxSubmitLink submit = new AjaxSubmitLink("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {

            }
        };


        // Construct a radio button and label for each company.
        group.add(new ListView<Patient>("choice", model) {
            protected void populateItem(ListItem<Patient> it) {

                it.add(new Radio("radio", it.getModel()));
                it.add(new Label("id", Long.toString(it.getModelObject().getId())));
                it.add(new Label("nhsNo", it.getModelObject().getNhsno()));
                it.add(new Label("forename", it.getModelObject().getForename()));
                it.add(new Label("surname", it.getModelObject().getSurname()));
                it.add(new Label("dateOfBirth", it.getModelObject().getDateofbirth()));
                it.add(new Label("unitCode", it.getModelObject().getUnitcode()));
            }
        });

        form.add(submit);
        form.add(group);

        return form;

    }

    private Form createCreateForm() {

        // This is the form that submits to the new create patient form

        AjaxSubmitLink create = new AjaxSubmitLink("create") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }

        };

        Form<?> createForm = new Form<Patient>("patientCreationForm") {
            @Override
            protected void onSubmit() {
                Patient patient = new Patient();
                patient.setDiseaseGroup(patientModel.getDiseaseGroup());
                patient.setNhsno(patientModel.getPatientId());
                patient.setNhsNumberType(patientModel.getNhsNumberType());

                setResponsePage(getDiseasePage(patient.getDiseaseGroup(), patient));

            }
        };
        createForm.add(create);

        return createForm;
    }

    public void setPatientModel(AddPatientModel patientModel) {
        this.patientModel = patientModel;
    }
}
