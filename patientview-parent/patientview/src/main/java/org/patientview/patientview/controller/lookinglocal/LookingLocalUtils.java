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

package org.patientview.patientview.controller.lookinglocal;

import org.apache.commons.collections.CollectionUtils;
import org.patientview.model.Patient;
import org.patientview.model.Unit;
import org.patientview.patientview.PatientDetails;
import org.patientview.patientview.comment.CommentUtils;
import org.patientview.patientview.controller.Routes;
import org.patientview.patientview.medicine.MedicineWithShortName;
import org.patientview.patientview.model.EdtaCode;
import org.patientview.patientview.model.Letter;
import org.patientview.patientview.model.Medicine;
import org.patientview.patientview.model.Panel;
import org.patientview.patientview.model.TestResultWithUnitShortname;
import org.patientview.patientview.model.User;
import org.patientview.patientview.unit.UnitUtils;
import org.patientview.patientview.user.UserUtils;
import org.patientview.service.EdtaCodeManager;
import org.patientview.utils.LegacySpringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Generate the xml for Looking local
 */
public final class LookingLocalUtils {

    public static final int OPTION_1 = 1;
    public static final int OPTION_2 = 2;
    public static final int OPTION_3 = 3;
    public static final int OPTION_4 = 4;
    public static final int OPTION_5 = 5;
    public static final int OPTION_6 = 6;
    public static final int OPTION_7 = 7;

    public static final int MAX_WORD_SIZE = 10;

    private LookingLocalUtils() {
    }

    /**
     * Utility function, creates empty XML document
     * @return Empty XML document
     * @throws Exception
     */
    public static Document getDocument() throws Exception {

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root element: screen
        Document doc = docBuilder.newDocument();
        Element screenElement = doc.createElement("screen");
        screenElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        screenElement.setAttribute("xsi:noNamespaceSchemaLocation", "http://www.digitv.gov.uk/schemas/plugin.xsd");
        doc.appendChild(screenElement);

        return doc;
    }

    /**
     * Write xml document to HTTP response
     * @param doc Input XML to output to HTTP response
     * @param response HTTP response
     * @throws Exception
     */
    public static void outputXml(Document doc, HttpServletResponse response) throws Exception {

        // output string
        DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty("encoding", "ISO-8859-1");
        transformer.transform(domSource, result);

        String sb = writer.toString();

        response.setContentType("text/xml");
        response.setContentLength(sb.length());
        PrintWriter out;
        out = response.getWriter();
        out.println(sb.toString());
        out.close();
        out.flush();
    }

    private static String getDiagnosisDescription(String code, EdtaCodeManager edtaCodeManager) {
        EdtaCode edtaCode = edtaCodeManager.getEdtaCode(code);
        if (edtaCode != null) {
            return edtaCode.getDescription();
        } else {
            return code;
        }
    }

    /**
     * Create XML for the My Details screen in Looking Local
     * @param request HTTP request
     * @param response HTTP response
     * @throws Exception
     */
    public static void getMyDetailsXml(HttpServletRequest request, HttpServletResponse response, int page,
                                       EdtaCodeManager edtaCodeManager)
            throws Exception {

        User user = UserUtils.retrieveUser(request);
        List<PatientDetails> patientDetails =
                LegacySpringUtils.getPatientManager().getPatientDetails(user.getUsername());
        Document doc = getDocument();

        // add page to screen
        Element pageElement = doc.createElement("page");
        pageElement.setAttribute("title", "My Details");
        pageElement.setAttribute("transform", "default");
        doc.getElementsByTagName("screen").item(0).appendChild(pageElement);

        // add form to screen
        Element formElement = doc.createElement("form");
        formElement.setAttribute("action", Routes.SERVER_URL + Routes.LOOKING_LOCAL_MY_DETAILS);
        formElement.setAttribute("method", "post");
        pageElement.appendChild(formElement);

        // if patient details exist, get first set of patient details and display on screen
        if (!CollectionUtils.isEmpty(patientDetails)) {
            Patient patient = patientDetails.get(0).getPatient();

            if (page == 0) {
                // first page
                Element name = doc.createElement("static");
                name.setAttribute("value", "Name: "
                        + (patient.getForename() != null ? patient.getForename() : "unavailable")
                        + (patient.getSurname() != null ? " " + patient.getSurname() : ""));
                formElement.appendChild(name);

                Element dob = doc.createElement("static");
                dob.setAttribute("value", "Date of Birth: "
                       + (patient.getFormatedDateOfBirth() != null ? patient.getFormatedDateOfBirth() : "unavailable"));
                formElement.appendChild(dob);

                Element nhsNo = doc.createElement("static");
                nhsNo.setAttribute("value", "NHS Number: "
                        + (patient.getNhsno() != null ? patient.getNhsno() : "unavailable"));
                formElement.appendChild(nhsNo);

                Element hospitalNo = doc.createElement("static");
                hospitalNo.setAttribute("value", "Hospital Number: "
                        + (patient.getHospitalnumber() != null ? patient.getHospitalnumber() : "unavailable"));
                formElement.appendChild(hospitalNo);

                Element address = doc.createElement("static");
                address.setAttribute("value", "Address: "
                        + (patient.getAddress1() != null ? patient.getAddress1()  + ", " : "unavailable")
                        + (patient.getAddress2() != null ? patient.getAddress2()  + ", " : " ")
                        + (patient.getAddress3() != null ? patient.getAddress3()  + ", " : " ")
                        + (patient.getAddress4() != null ? patient.getAddress4() : " "));
                formElement.appendChild(address);

                Element telephone1 = doc.createElement("static");
                telephone1.setAttribute("value", "Telephone 1: "
                        + (patient.getTelephone1() != null ? patient.getTelephone1() : "unavailable"));
                formElement.appendChild(telephone1);

                Element telephone2 = doc.createElement("static");
                telephone2.setAttribute("value", "Telephone 2: "
                        + (patient.getTelephone2() != null ? patient.getTelephone2() : "unavailable"));
                formElement.appendChild(telephone2);

                Element mobile = doc.createElement("static");
                mobile.setAttribute("value", "Mobile: "
                        + (patient.getMobile() != null ? patient.getMobile() : "unavailable"));
                formElement.appendChild(mobile);

                Element diagnosis = doc.createElement("static");
                diagnosis.setAttribute("value", "Diagnosis: "
                        + (patient.getDiagnosis() != null
                        ? getDiagnosisDescription(patient.getDiagnosis(), edtaCodeManager) : "unavailable"));
                formElement.appendChild(diagnosis);

            } else {
                // second page
                Element gpName = doc.createElement("static");
                gpName.setAttribute("value", "GP Name: "
                        + (patient.getGpname() != null ? patient.getGpname() : "unavailable"));
                formElement.appendChild(gpName);

                Element gpTelephone = doc.createElement("static");
                gpTelephone.setAttribute("value", "GP Telephone: "
                        + (patient.getGptelephone() != null ? patient.getGptelephone() : "unavailable"));
                formElement.appendChild(gpTelephone);

                Element gpAddress = doc.createElement("static");
                gpAddress.setAttribute("value", "Address: "
                        + (patient.getGpaddress1() != null ? patient.getGpaddress1()  + ", " : "unavailable")
                        + (patient.getGpaddress2() != null ? patient.getGpaddress2()  + ", " : " ")
                        + (patient.getGpaddress3() != null ? patient.getGpaddress3() : " "));
                formElement.appendChild(gpAddress);

                Element treatment = doc.createElement("static");
                treatment.setAttribute("value", "Treatment: "
                        + (patient.getTreatment() != null ? patient.getTreatment() : "unavailable"));
                formElement.appendChild(treatment);

                Element transplantStatus = doc.createElement("static");
                transplantStatus.setAttribute("value", "Transplant status: "
                        + (patient.getTransplantstatus() != null ? patient.getTransplantstatus() : "unavailable"));
                formElement.appendChild(transplantStatus);

                Element otherConditions = doc.createElement("static");
                otherConditions.setAttribute("value", "Other conditions: "
                        + (patient.getOtherConditions() != null ? patient.getOtherConditions() : "unavailable"));
                formElement.appendChild(otherConditions);
            }
        } else {
            // no patient details found for this user, put error message
            Element errorMessage = doc.createElement("static");
            errorMessage.setAttribute("value", "The 'My Details' page is for patient information only.");
            formElement.appendChild(errorMessage);
        }

        // back button
        Element back = doc.createElement("submit");
        back.setAttribute("name", "left");
        back.setAttribute("title", "Back");
        formElement.appendChild(back);

        if (page == 0) {
            // more button
            Element more = doc.createElement("submit");
            more.setAttribute("name", "right");
            more.setAttribute("title", "More");
            formElement.appendChild(more);
        }

        // form action
        Element formAction = doc.createElement("hiddenField");
        formAction.setAttribute("name", "formAction");
        formAction.setAttribute("value", Routes.SERVER_URL + Routes.LOOKING_LOCAL_MY_DETAILS);
        formElement.appendChild(formAction);

        // form method
        Element formMethod = doc.createElement("hiddenField");
        formMethod.setAttribute("name", "formMethod");
        formMethod.setAttribute("value", "post");
        formElement.appendChild(formMethod);

        outputXml(doc, response);
    }

    /**
     * Create XML for the Drugs screen in Looking Local
     * @param request HTTP request
     * @param response HTTP response
     * @throws Exception
     */
    public static void getDrugsXml(HttpServletRequest request, HttpServletResponse response, int page, int itemsPerPage)
            throws Exception {

        boolean lastPage = false;

        User user = UserUtils.retrieveUser(request);
        List<MedicineWithShortName> medicineWithShortNames = getMedicinesForPatient(user);

        Document doc = getDocument();
        // add page to screen
        Element pageElement = doc.createElement("page");
        pageElement.setAttribute("title", "Drugs");
        pageElement.setAttribute("transform", "default");
        doc.getElementsByTagName("screen").item(0).appendChild(pageElement);

        // add form to screen
        Element formElement = doc.createElement("form");
        formElement.setAttribute("action", Routes.SERVER_URL + Routes.LOOKING_LOCAL_DRUGS);
        formElement.setAttribute("method", "post");
        pageElement.appendChild(formElement);

        if (medicineWithShortNames != null && !medicineWithShortNames.isEmpty()) {
            StringBuffer sb;
            int totalItems = medicineWithShortNames.size();
            int start = page * itemsPerPage;
            int end = start + itemsPerPage;
            if (end > totalItems) {
                lastPage = true;
                end = totalItems;
            }

            // set paging at top
            Double totalPages = Math.floor((double) totalItems / (double) itemsPerPage) + 1;
            formElement.setAttribute("pagingText", "page " + (page + 1) + " of " + totalPages.intValue());

            List<MedicineWithShortName> selection = medicineWithShortNames.subList(start, end);

            if (!selection.isEmpty()) {
                // static element
                Element name = doc.createElement("static");
                name.setAttribute("value", "Start Date   |   Medicine name   | Dose   | Source");
                formElement.appendChild(name);

                for (MedicineWithShortName medicine : selection) {
                    Element medicineEl = doc.createElement("static");
                    sb = new StringBuffer();
                    sb.append(medicine.getFormattedStartDate(true)).append(" ");
                    sb.append(medicine.getName()).append(" ");
                    sb.append(medicine.getDose()).append(" ");
                    sb.append(medicine.getShortname());
                    medicineEl.setAttribute("value", sb.toString());
                    formElement.appendChild(medicineEl);
                }
            } else {
                Element data = doc.createElement("static");
                data.setAttribute("value", "End of drug list");
                formElement.appendChild(data);

                data = doc.createElement("static");
                data.setAttribute("value", totalItems + " total drugs");
                formElement.appendChild(data);
            }
        }

        // back button
        Element back = doc.createElement("submit");
        back.setAttribute("name", "left");
        back.setAttribute("title", "Back");
        formElement.appendChild(back);

        if (!lastPage) {
            // more button
            Element more = doc.createElement("submit");
            more.setAttribute("name", "right");
            more.setAttribute("title", "More");
            formElement.appendChild(more);
        }

        // form action
        Element formAction = doc.createElement("hiddenField");
        formAction.setAttribute("name", "formAction");
        formAction.setAttribute("value", Routes.SERVER_URL + Routes.LOOKING_LOCAL_DRUGS);
        formElement.appendChild(formAction);

        // form method
        Element formMethod = doc.createElement("hiddenField");
        formMethod.setAttribute("name", "formMethod");
        formMethod.setAttribute("value", "post");
        formElement.appendChild(formMethod);

        outputXml(doc, response);
    }

    /**
     * Get patient medicines, including unit based on User
     * @param user Patient/logged in User
     * @return List of MedicineWithShortName medicines
     * @throws Exception
     */
    private static List<MedicineWithShortName> getMedicinesForPatient(User user)
            throws Exception {
        List<MedicineWithShortName> medicinesWithShortName = new ArrayList<MedicineWithShortName>();
        if (user != null) {
            List<Medicine> medicines = LegacySpringUtils.getMedicineManager().getUserMedicines(user);
            if (medicines != null) {
                for (Medicine med : medicines) {
                    Unit unit = UnitUtils.retrieveUnit(med.getUnitcode());
                    if (unit != null) {
                        medicinesWithShortName.add(new MedicineWithShortName(med, unit.getShortname()));
                    } else {
                        medicinesWithShortName.add(new MedicineWithShortName(med, "UNKNOWN UNIT:" + med.getUnitcode()));
                    }
                }
            }
        }

        return medicinesWithShortName;
    }

    /**
     * Create XML for the Result screen in Looking Local
     * @param request HTTP request
     * @param response HTTP response
     * @throws Exception
     */
    public static void getMedicalResultsXml(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Document doc = getDocument();

        // add page to screen
        Element pageElement = doc.createElement("page");
        pageElement.setAttribute("title", "Results");
        pageElement.setAttribute("transform", "default");
        doc.getElementsByTagName("screen").item(0).appendChild(pageElement);

        // add form to screen
        Element formElement = doc.createElement("form");
        formElement.setAttribute("action", Routes.SERVER_URL + Routes.LOOKING_LOCAL_RESULTS_DISPLAY);
        formElement.setAttribute("method", "post");
        //formElement.setAttribute("pagingText", "Page 1 of 3");
        pageElement.appendChild(formElement);

        // static element
        Element details = doc.createElement("static");
        details.setAttribute("value", "Select the results to view:");
        formElement.appendChild(details);

        //  multisubmitField
        Element multisubmit = doc.createElement("multisubmitField");
        multisubmit.setAttribute("name", "selection");
        formElement.appendChild(multisubmit);

        // Urea field Option
        Element fieldOption1 = doc.createElement("fieldOption");
        fieldOption1.setAttribute("name", "Urea");
        fieldOption1.setAttribute("value", "1");
        multisubmit.appendChild(fieldOption1);

        // Creatininefield Option
        Element fieldOption2 = doc.createElement("fieldOption");
        fieldOption2.setAttribute("name", "Creatinine");
        fieldOption2.setAttribute("value", "2");
        multisubmit.appendChild(fieldOption2);

        // Potassium field Option
        Element fieldOption3 = doc.createElement("fieldOption");
        fieldOption3.setAttribute("name", "Potassium");
        fieldOption3.setAttribute("value", "3");
        multisubmit.appendChild(fieldOption3);

        // Calcium field Option
        Element fieldOption4 = doc.createElement("fieldOption");
        fieldOption4.setAttribute("name", "Calcium");
        fieldOption4.setAttribute("value", "4");
        multisubmit.appendChild(fieldOption4);

        // PO4 field Option
        Element fieldOption5 = doc.createElement("fieldOption");
        fieldOption5.setAttribute("name", "PO4");
        fieldOption5.setAttribute("value", "5");
        multisubmit.appendChild(fieldOption5);

        // Hb field Option
        Element fieldOption6 = doc.createElement("fieldOption");
        fieldOption6.setAttribute("name", "Hb");
        fieldOption6.setAttribute("value", "6");
        multisubmit.appendChild(fieldOption6);

        // Hb field Option
        Element fieldOption7 = doc.createElement("fieldOption");
        fieldOption7.setAttribute("name", "Wbc");
        fieldOption7.setAttribute("value", "7");
        multisubmit.appendChild(fieldOption7);

        // back button
        Element back = doc.createElement("submit");
        back.setAttribute("name", "left");
        back.setAttribute("title", "Back");
        formElement.appendChild(back);

        // form action
        Element formAction = doc.createElement("hiddenField");
        formAction.setAttribute("name", "formAction");
        formAction.setAttribute("value", Routes.SERVER_URL + Routes.LOOKING_LOCAL_RESULTS_DISPLAY);
        formElement.appendChild(formAction);

        // form method
        Element formMethod = doc.createElement("hiddenField");
        formMethod.setAttribute("name", "formMethod");
        formMethod.setAttribute("value", "post");
        formElement.appendChild(formMethod);

        outputXml(doc, response);
    }

    /**
     * Create XML for the letter details screen in Looking Local based on letter ID
     * @param request HTTP request
     * @param response HTTP response
     * @param selection ID of letter to show on screen, chosen by user
     * @throws Exception
     */
    public static void getLetterDetailsXml(HttpServletRequest request, HttpServletResponse response,
                                           String selection, int page, int linesPerPage, int lineLength)
                                        throws Exception {
        boolean lastPage = false;

        Letter letter = LegacySpringUtils.getLetterManager().get(Long.parseLong(selection));
        boolean permissionToReadLetter = false;
        if (letter != null && letter.getNhsno() != null) {
            permissionToReadLetter = CommentUtils.verifyPermissionToReadItem(request, letter.getNhsno());
        }

        Document doc = getDocument();
        // add page to screen
        Element pageElement = doc.createElement("page");
        pageElement.setAttribute("transform", "default");
        doc.getElementsByTagName("screen").item(0).appendChild(pageElement);

        // add form to screen
        Element formElement = doc.createElement("form");
        formElement.setAttribute("action", Routes.SERVER_URL + Routes.LOOKING_LOCAL_LETTER_DISPLAY);
        formElement.setAttribute("method", "post");
        pageElement.appendChild(formElement);

        if (permissionToReadLetter && letter != null) {
            pageElement.setAttribute("title", letter.getFormattedDate() + " " + letter.getType());

            // display letter using static elements
            String letterContent = letter.getContent();

            // create list of all lines
            String[] allLines = letterContent.split("\n");
            List<String> allLineList = Arrays.asList(allLines);
            List<String> finalLineList = new ArrayList<String>();

            // for each paragraph (as have split by \n) make sure each line only lineLength
            // long by adding new line after current line
            for (int i = 0; i < allLineList.size(); i++) {
                List<String> thisLine = new ArrayList<String>();
                thisLine.add(allLineList.get(i));

                for (int j = 0; j < thisLine.size(); j++) {
                    if (thisLine.get(j).length() > lineLength) {

                        // handle splitting paragraph and not splitting words
                        int firstSpace;

                        if ((lineLength - MAX_WORD_SIZE) > thisLine.get(j).length()) {
                            firstSpace = thisLine.get(j).length();
                        } else {
                            firstSpace = thisLine.get(j).indexOf(" ", lineLength - MAX_WORD_SIZE);
                        }

                        if (firstSpace == -1) {
                            firstSpace = lineLength;
                        }

                        // add new line from linelength to firstSpace
                        thisLine.add(j + 1, thisLine.get(j).substring(firstSpace, thisLine.get(j).length()).trim());

                        // clip this element
                        thisLine.set(j, thisLine.get(j).substring(0, firstSpace));
                        j--;
                    }
                }

                finalLineList.addAll(thisLine);
            }

            int totalItems = finalLineList.size();
            int start = page * linesPerPage;
            int end = start + linesPerPage;
            if (end > totalItems) {
                lastPage = true;
                end = totalItems;
            }

            // set paging at top
            Double totalPages = Math.floor((double) totalItems / (double) linesPerPage) + 1;
            formElement.setAttribute("pagingText", "page " + (page + 1) + " of " + totalPages.intValue());

            List<String> letterSelection = finalLineList.subList(start, end);

            if (letterSelection.isEmpty()) {
                Element content = doc.createElement("static");
                content.setAttribute("value", "");
                formElement.appendChild(content);

                content = doc.createElement("static");
                content.setAttribute("value", "End of letter");
                formElement.appendChild(content);

                content = doc.createElement("static");
                content.setAttribute("value", "Sent " + letter.getFormattedDate());
                formElement.appendChild(content);
            }

            for (String line : letterSelection) {
                Element content = doc.createElement("static");
                content.setAttribute("value", line);
                formElement.appendChild(content);
            }
        }

        // back button
        Element back = doc.createElement("submit");
        back.setAttribute("name", "left");
        back.setAttribute("title", "Back");
        formElement.appendChild(back);

        if (!lastPage) {
            // more button
            Element more = doc.createElement("submit");
            more.setAttribute("name", "right");
            more.setAttribute("title", "More");
            formElement.appendChild(more);
        }

        // form action
        Element formAction = doc.createElement("hiddenField");
        formAction.setAttribute("name", "formAction");
        formAction.setAttribute("value", Routes.SERVER_URL + Routes.LOOKING_LOCAL_LETTER_DISPLAY);
        formElement.appendChild(formAction);

        // form method
        Element formMethod = doc.createElement("hiddenField");
        formMethod.setAttribute("name", "formMethod");
        formMethod.setAttribute("value", "post");
        formElement.appendChild(formMethod);

        outputXml(doc, response);
    }

    /**
     * Create XML for the Select Letter screen in Looking Local, displays list of patient letters
     * @param request HTTP request
     * @param response HTTP response
     * @throws Exception
     */
    public static void getLettersXml(HttpServletRequest request, HttpServletResponse response,
                                     int page, int itemsPerPage) throws Exception {
        boolean lastPage = false;
        User user = UserUtils.retrieveUser(request);
        List<Letter> letters = LegacySpringUtils.getLetterManager().get(user.getUsername());

        Document doc = getDocument();
        // add page to screen
        Element pageElement = doc.createElement("page");
        pageElement.setAttribute("title", "Select Letter");
        pageElement.setAttribute("transform", "default");
        doc.getElementsByTagName("screen").item(0).appendChild(pageElement);

        // add form to screen
        Element formElement = doc.createElement("form");
        formElement.setAttribute("action", Routes.SERVER_URL + Routes.LOOKING_LOCAL_LETTERS);
        formElement.setAttribute("method", "post");
        pageElement.appendChild(formElement);

        if (letters != null && !letters.isEmpty()) {
            // static element
            Element details = doc.createElement("static");
            details.setAttribute("value", "Select the letter to view:");
            formElement.appendChild(details);

            Element heading = doc.createElement("static");
            heading.setAttribute("value", "Date        | Letter ");
            formElement.appendChild(heading);

            //  multisubmitField
            Element multisubmit = doc.createElement("multisubmitField");
            multisubmit.setAttribute("name", "selection");
            formElement.appendChild(multisubmit);

            StringBuffer sb;
            int totalItems = letters.size();
            int start = page * itemsPerPage;
            int end = start + itemsPerPage;
            if (end > totalItems) {
                lastPage = true;
                end = totalItems;
            }

            // set paging at top
            Double totalPages = Math.floor((double) totalItems / (double) itemsPerPage) + 1;
            formElement.setAttribute("pagingText", "page " + (page + 1) + " of " + totalPages.intValue());

            List<Letter> selection = letters.subList(start, end);

            for (Letter letter : selection) {
                Element fieldOption = doc.createElement("fieldOption");
                sb = new StringBuffer();
                sb.append(letter.getFormattedDate()).append(" ");
                sb.append(letter.getType());
                fieldOption.setAttribute("name", sb.toString());
                fieldOption.setAttribute("value", letter.getId().toString());
                multisubmit.appendChild(fieldOption);
            }
        }

        // back button
        Element back = doc.createElement("submit");
        back.setAttribute("name", "left");
        back.setAttribute("title", "Back");
        formElement.appendChild(back);

        if (!lastPage) {
            // more button
            Element more = doc.createElement("submit");
            more.setAttribute("name", "right");
            more.setAttribute("title", "More");
            formElement.appendChild(more);
        }

        // form action
        Element formAction = doc.createElement("hiddenField");
        formAction.setAttribute("name", "formAction");
        formAction.setAttribute("value", Routes.SERVER_URL + Routes.LOOKING_LOCAL_LETTERS);
        formElement.appendChild(formAction);

        // form method
        Element formMethod = doc.createElement("hiddenField");
        formMethod.setAttribute("name", "formMethod");
        formMethod.setAttribute("value", "post");
        formElement.appendChild(formMethod);

        outputXml(doc, response);
    }

    /**
     * Create XML for the result details screen in Looking Local
     * @param request HTTP request
     * @param response HTTP response
     * @param selection Result ID to display, chosen by user from list
     * @throws Exception
     */
    public static void getResultsDetailsXml(HttpServletRequest request,
                                            HttpServletResponse response, String selection, int page, int itemsPerPage)
            throws Exception {

        boolean lastPage = false;

        User user = UserUtils.retrieveUser(request);
        List<TestResultWithUnitShortname> results
                = LegacySpringUtils.getTestResultManager().getTestResultForPatient(user, new Panel(1));

        Document doc = getDocument();
        // add page to screen
        Element pageElement = doc.createElement("page");
        pageElement.setAttribute("transform", "default");
        doc.getElementsByTagName("screen").item(0).appendChild(pageElement);

        // add form to screen
        Element formElement = doc.createElement("form");
        formElement.setAttribute("action", Routes.SERVER_URL + Routes.LOOKING_LOCAL_RESULT_DISPLAY);
        formElement.setAttribute("method", "post");
        pageElement.appendChild(formElement);

        List<TestResultWithUnitShortname> filterTestResults;
        switch (Integer.parseInt(selection)) {
            case OPTION_1 : filterTestResults = filterTestResults(results, "urea");
                pageElement.setAttribute("title", "Urea");
                break;
            case OPTION_2 : filterTestResults = filterTestResults(results, "creatinine");
                pageElement.setAttribute("title", "Creatinine");
                break;
            case OPTION_3 : filterTestResults = filterTestResults(results, "potassium");
                pageElement.setAttribute("title", "Potassium");
                break;
            case OPTION_4 : filterTestResults = filterTestResults(results, "calcium");
                pageElement.setAttribute("title", "Calcium");
                break;
            case OPTION_5 : filterTestResults = filterTestResults(results, "phosphate");
                pageElement.setAttribute("title", "PO4");
                break;
            case OPTION_6 : filterTestResults = filterTestResults(results, "hb");
                pageElement.setAttribute("title", "Hb");
                break;
            case OPTION_7 : filterTestResults = filterTestResults(results, "wbc");
                pageElement.setAttribute("title", "Wbc");
                break;
            default:filterTestResults = null;
                break;
        }

        if (filterTestResults != null && !filterTestResults.isEmpty()) {

            Element heading = doc.createElement("static");
            heading.setAttribute("value", "Date     |    Value");
            formElement.appendChild(heading);

            StringBuffer sb;
            int totalItems = filterTestResults.size();
            int start = page * itemsPerPage;
            int end = start + itemsPerPage;
            if (end > totalItems) {
                lastPage = true;
                end = totalItems;
            }

            // set paging at top
            Double totalPages = Math.floor((double) totalItems / (double) itemsPerPage) + 1;
            formElement.setAttribute("pagingText", "page " + (page + 1) + " of " + totalPages.intValue());

            List<TestResultWithUnitShortname> resultSelection = filterTestResults.subList(start, end);

            for (TestResultWithUnitShortname result : resultSelection) {
                // static element
                Element data = doc.createElement("static");
                sb = new StringBuffer();
                sb.append(result.getFormattedDatestamp()).append(" ");
                if (result.getPrepost() != null) {
                    sb.append(result.getPrepost()).append(" ");
                } else {
                    sb.append("      ");
                }
                sb.append(result.getValue());

                data.setAttribute("value", sb.toString());
                formElement.appendChild(data);
            }
        } else {
            lastPage = true;
            Element data = doc.createElement("static");
            data.setAttribute("value", "No Results Available");
            formElement.appendChild(data);
        }

        // back button
        Element back = doc.createElement("submit");
        back.setAttribute("name", "left");
        back.setAttribute("title", "Back");
        formElement.appendChild(back);

        if (!lastPage) {
            // more button
            Element more = doc.createElement("submit");
            more.setAttribute("name", "right");
            more.setAttribute("title", "More");
            formElement.appendChild(more);
        }

        // form action
        Element formAction = doc.createElement("hiddenField");
        formAction.setAttribute("name", "formAction");
        formAction.setAttribute("value", Routes.SERVER_URL + Routes.LOOKING_LOCAL_RESULT_DISPLAY);
        formElement.appendChild(formAction);

        // form method
        Element formMethod = doc.createElement("hiddenField");
        formMethod.setAttribute("name", "formMethod");
        formMethod.setAttribute("value", "post");
        formElement.appendChild(formMethod);

        outputXml(doc, response);
    }

    /**
     * Filter patient test results based on test code
     * @param results List of TestResultWithUnitShortname patient test results
     * @param testCode Test code to filter results by
     * @return List of filtered test results
     */
    private static List<TestResultWithUnitShortname> filterTestResults(List<TestResultWithUnitShortname> results,
                                                                       String testCode) {

        List<TestResultWithUnitShortname> filterResults = new ArrayList<TestResultWithUnitShortname>();
        for (TestResultWithUnitShortname result : results) {
            if (testCode.equals(result.getTestcode())) {
                filterResults.add(result);
            }
        }
        return filterResults;
    }

    /**
     * Create XML for the home screen in Looking Local
     * @param response HTTP response
     * @throws Exception
     */
    public static void getHomeXml(HttpServletResponse response) throws Exception {

        Document doc = getDocument();
        // add page to screen
        Element pageElement = doc.createElement("page");
        pageElement.setAttribute("title", "PatientView (PV) – view your results");
        pageElement.setAttribute("transform", "default");
        doc.getElementsByTagName("screen").item(0).appendChild(pageElement);

        // add form to screen
        Element formElement = doc.createElement("form");
        formElement.setAttribute("action", Routes.SERVER_URL + Routes.LOOKING_LOCAL_AUTH);
        formElement.setAttribute("method", "post");
        formElement.setAttribute("name", "blank");
        pageElement.appendChild(formElement);

        Element details = doc.createElement("static");
        details.setAttribute("value", "Please key in your details:");
        formElement.appendChild(details);

        Element username = doc.createElement("textField");
        username.setAttribute("hint", "Enter your username");
        username.setAttribute("label", "Username:");
        username.setAttribute("name", "username");
        username.setAttribute("size", "10");
        username.setAttribute("value", "");
        formElement.appendChild(username);

        Element password = doc.createElement("textField");
        password.setAttribute("hint", "Enter your Password");
        password.setAttribute("label", "Password:");
        password.setAttribute("name", "password");
        password.setAttribute("password", "true");
        password.setAttribute("size", "10");
        password.setAttribute("value", "");
        formElement.appendChild(password);

        // static element
        Element forget = doc.createElement("static");
        forget.setAttribute("value", "If you have forgotten your password, "
                + "please contact your unit administrator.");
        formElement.appendChild(forget);

        // sign-in button
        Element signIn = doc.createElement("submit");
        signIn.setAttribute("name", "left");
        signIn.setAttribute("title", "Sign in");
        formElement.appendChild(signIn);

        // form action
        Element formAction = doc.createElement("hiddenField");
        formAction.setAttribute("name", "formAction");
        formAction.setAttribute("value", Routes.SERVER_URL + Routes.LOOKING_LOCAL_AUTH);
        formElement.appendChild(formAction);

        // form method
        Element formMethod = doc.createElement("hiddenField");
        formMethod.setAttribute("name", "formMethod");
        formMethod.setAttribute("value", "post");
        formElement.appendChild(formMethod);

        outputXml(doc, response);
    }

    /**
     * Create XML for the main screen in Looking Local with core options
     * @param response HTTP response
     * @throws Exception
     */
    public static void getMainXml(HttpServletResponse response) throws Exception {

        Document doc = getDocument();
        // add page to screen
        Element pageElement = doc.createElement("page");
        pageElement.setAttribute("title", "PatientView");
        pageElement.setAttribute("transform", "default");
        doc.getElementsByTagName("screen").item(0).appendChild(pageElement);

        // add form to screen
        Element formElement = doc.createElement("form");
        formElement.setAttribute("action", Routes.SERVER_URL + Routes.LOOKING_LOCAL_DETAILS);
        formElement.setAttribute("method", "post");
        pageElement.appendChild(formElement);

        // static element
        Element details = doc.createElement("static");
        details.setAttribute("value", "Select what you would like to look at:");
        formElement.appendChild(details);

        //  multisubmitField
        Element multisubmit = doc.createElement("multisubmitField");
        multisubmit.setAttribute("name", "selection");
        formElement.appendChild(multisubmit);

        // my details field Option
        Element fieldOption1 = doc.createElement("fieldOption");
        fieldOption1.setAttribute("name", "My Details");
        fieldOption1.setAttribute("value", "1");
        multisubmit.appendChild(fieldOption1);

        // medical result field Option
        Element fieldOption2 = doc.createElement("fieldOption");
        fieldOption2.setAttribute("name", "Medical Results");
        fieldOption2.setAttribute("value", "2");
        multisubmit.appendChild(fieldOption2);

        // drugs field Option
        Element fieldOption3 = doc.createElement("fieldOption");
        fieldOption3.setAttribute("name", "Drugs");
        fieldOption3.setAttribute("value", "3");
        multisubmit.appendChild(fieldOption3);

        // letters field Option
        Element fieldOption4 = doc.createElement("fieldOption");
        fieldOption4.setAttribute("name", "Letters");
        fieldOption4.setAttribute("value", "4");
        multisubmit.appendChild(fieldOption4);

        // form action
        Element formAction = doc.createElement("hiddenField");
        formAction.setAttribute("name", "formAction");
        formAction.setAttribute("value", Routes.SERVER_URL + Routes.LOOKING_LOCAL_DETAILS);
        formElement.appendChild(formAction);

        // form method
        Element formMethod = doc.createElement("hiddenField");
        formMethod.setAttribute("name", "formMethod");
        formMethod.setAttribute("value", "post");
        formElement.appendChild(formMethod);

        outputXml(doc, response);
    }

    /**
     * Create XML for the error page in Looking Local
     * @param response HTTP response
     * @throws Exception
     */
    public static void getErrorXml(HttpServletResponse response, String message) throws Exception {

        Document doc = getDocument();
        // add page to screen
        Element pageElement = doc.createElement("page");
        pageElement.setAttribute("title", "There has been an error");
        pageElement.setAttribute("transform", "default");
        doc.getElementsByTagName("screen").item(0).appendChild(pageElement);

        // add form to screen
        Element formElement = doc.createElement("form");
        formElement.setAttribute("action", Routes.SERVER_URL + Routes.LOOKING_LOCAL_HOME);
        formElement.setAttribute("method", "get");
        pageElement.appendChild(formElement);

        // static element
        Element details = doc.createElement("static");
        details.setAttribute("value", "There has been an error. " + message);
        formElement.appendChild(details);

        // home button
        Element home = doc.createElement("submit");
        home.setAttribute("name", "left");
        home.setAttribute("title", "Home");
        formElement.appendChild(home);

        // form action
        Element formAction = doc.createElement("hiddenField");
        formAction.setAttribute("name", "formAction");
        formAction.setAttribute("value", Routes.SERVER_URL + Routes.LOOKING_LOCAL_HOME);
        formElement.appendChild(formAction);

        // form method
        Element formMethod = doc.createElement("hiddenField");
        formMethod.setAttribute("name", "formMethod");
        formMethod.setAttribute("value", "get");
        formElement.appendChild(formMethod);

        outputXml(doc, response);
    }

    /**
     * Create XML for the authentication error page in Looking Local
     * @param response HTTP response
     * @throws Exception
     */
    public static void getAuthErrorXml(HttpServletResponse response) throws Exception {

        Document doc = getDocument();
        // add page to screen
        Element pageElement = doc.createElement("page");
        pageElement.setAttribute("title", "There has been an authentication error");
        pageElement.setAttribute("transform", "default");
        doc.getElementsByTagName("screen").item(0).appendChild(pageElement);

        // add form to screen
        Element formElement = doc.createElement("form");
        formElement.setAttribute("action", Routes.SERVER_URL + Routes.LOOKING_LOCAL_HOME);
        formElement.setAttribute("method", "get");
        pageElement.appendChild(formElement);

        // static element
        Element details = doc.createElement("static");
        details.setAttribute("value", "We're sorry, the username/password combination was not recognised. "
                + "Please try again");
        formElement.appendChild(details);

        // home button
        Element home = doc.createElement("submit");
        home.setAttribute("name", "left");
        home.setAttribute("title", "Home");
        formElement.appendChild(home);

        // form action
        Element formAction = doc.createElement("hiddenField");
        formAction.setAttribute("name", "formAction");
        formAction.setAttribute("value", Routes.SERVER_URL + Routes.LOOKING_LOCAL_HOME);
        formElement.appendChild(formAction);

        // form method
        Element formMethod = doc.createElement("hiddenField");
        formMethod.setAttribute("name", "formMethod");
        formMethod.setAttribute("value", "get");
        formElement.appendChild(formMethod);

        outputXml(doc, response);
    }

    /**
     * Create XML for the Login Successful screen in Looking Local
     * @param response HTTP response
     * @throws Exception
     */
    public static void getAuthXml(HttpServletResponse response) throws Exception {

        Document doc = getDocument();
        // add page to screen
        Element pageElement = doc.createElement("page");
        pageElement.setAttribute("title", "Login Successful");
        pageElement.setAttribute("transform", "default");
        doc.getElementsByTagName("screen").item(0).appendChild(pageElement);

        // add form to screen
        Element formElement = doc.createElement("form");
        formElement.setAttribute("action", Routes.SERVER_URL + Routes.LOOKING_LOCAL_MAIN);
        formElement.setAttribute("method", "get");
        pageElement.appendChild(formElement);

        // static element
        Element details = doc.createElement("static");
        details.setAttribute("value", "You have successfully logged in.");
        formElement.appendChild(details);

        // home button
        Element home = doc.createElement("submit");
        home.setAttribute("name", "left");
        home.setAttribute("title", "Continue");
        formElement.appendChild(home);

        // form action
        Element formAction = doc.createElement("hiddenField");
        formAction.setAttribute("name", "formAction");
        formAction.setAttribute("value", Routes.SERVER_URL + Routes.LOOKING_LOCAL_MAIN);
        formElement.appendChild(formAction);

        // form method
        Element formMethod = doc.createElement("hiddenField");
        formMethod.setAttribute("name", "formMethod");
        formMethod.setAttribute("value", "get");
        formElement.appendChild(formMethod);

        outputXml(doc, response);
    }
}
