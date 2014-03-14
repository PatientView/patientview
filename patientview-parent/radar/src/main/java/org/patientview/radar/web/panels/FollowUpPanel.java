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

package org.patientview.radar.web.panels;


import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.patientview.radar.web.RadarApplication;
import org.patientview.radar.web.behaviours.RadarBehaviourFactory;
import org.patientview.radar.web.models.PageNumberModel;
import org.patientview.radar.web.pages.patient.srns.SrnsPatientPage;
import org.patientview.radar.web.panels.followup.ClinicalPicturePanel;
import org.patientview.radar.web.panels.followup.FollowUpLaboratoryResultsPanel;
import org.patientview.radar.web.panels.followup.FollowUpTreatmentPanel;
import org.patientview.radar.web.panels.followup.RrtTherapyPanel;

public class FollowUpPanel extends Panel {

    private ClinicalPicturePanel clinicalPicturePanel;
    private FollowUpLaboratoryResultsPanel laboratoryResults;
    private FollowUpTreatmentPanel treatmentPanel;
    private RrtTherapyPanel rrtTherapyPanel;

    private MarkupContainer linksContainer;

    public enum CurrentTab {
        CLINICAL_PICTURE(RadarApplication.CLINICAL_FOLLOW_UP_PAGE_NO),
        LABORATORY_RESULTS(RadarApplication.LABORATORY_FOLLOW_UP_PAGE_NO),
        TREATMENT(RadarApplication.TREATMENT_FOLLOW_UP_PAGE_NO),
        RRT_THERAPY(RadarApplication.RRT_THERAPY_PAGE_NO);

        private int pageNumber;

        CurrentTab(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public int getPageNumber() {
            return pageNumber;
        }
    }

    private CurrentTab currentTab = CurrentTab.CLINICAL_PICTURE;

    public FollowUpPanel(String id, IModel<Long> radarNumberModel) {
        super(id);
        setOutputMarkupId(true);
        setOutputMarkupPlaceholderTag(true);

        // Add a container for the links to update the highlighted tab
        linksContainer = new WebMarkupContainer("linksContainer");
        linksContainer.setOutputMarkupId(true);

        linksContainer.add(new TabAjaxLink("clinicalPictureLink", CurrentTab.CLINICAL_PICTURE));
        linksContainer.add(new TabAjaxLink("laboratoryResultsLink", CurrentTab.LABORATORY_RESULTS));
        linksContainer.add(new TabAjaxLink("treatmentLink", CurrentTab.TREATMENT));
        linksContainer.add(new TabAjaxLink("rrtTherapyLink", CurrentTab.RRT_THERAPY));

        clinicalPicturePanel = new ClinicalPicturePanel("clinicalPicturePanel", radarNumberModel);
        laboratoryResults = new FollowUpLaboratoryResultsPanel("laboratoryResultsPanel", radarNumberModel);
        treatmentPanel = new FollowUpTreatmentPanel("treatmentPanel", radarNumberModel);
        rrtTherapyPanel = new RrtTherapyPanel("rrtTherapyPanel", radarNumberModel);
        add(linksContainer, clinicalPicturePanel, laboratoryResults, treatmentPanel, rrtTherapyPanel);
    }

    public CurrentTab getCurrentTab() {
        return currentTab;
    }

    private class TabAjaxLink extends AjaxLink {
        private CurrentTab tab;

        public TabAjaxLink(String id, CurrentTab tab) {
            super(id);
            this.tab = tab;
            // Decorate span with class="hovered" if we're active tab
            MarkupContainer span = new WebMarkupContainer("span");
            span.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
                @Override
                public String getObject() {
                    return currentTab.equals(TabAjaxLink.this.tab) ? "hovered" : "";
                }
            }));

            add(span);
        }

        @Override
        public void onClick(AjaxRequestTarget target) {
            currentTab = tab;
            target.add(linksContainer, clinicalPicturePanel, laboratoryResults, treatmentPanel,
                    rrtTherapyPanel);

            Component pageNumber = getPage().get("pageNumber");
            PageNumberModel pageNumberModel = (PageNumberModel) pageNumber.getDefaultModel();
            pageNumberModel.setPageNumber(currentTab.getPageNumber());
            target.add(pageNumber);
        }

        @Override
        protected IAjaxCallDecorator getAjaxCallDecorator() {
            return RadarBehaviourFactory.getWarningOnFormExitCallDecorator();
        }
    }

    @Override
    public boolean isVisible() {
        return ((SrnsPatientPage) getPage()).getCurrentTab().equals(SrnsPatientPage.CurrentTab.FOLLOW_UP);
    }

}
