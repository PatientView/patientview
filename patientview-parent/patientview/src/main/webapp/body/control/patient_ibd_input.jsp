<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>


<html:xhtml/>

<div class="span9">
    <div class="page-header">
        <h1>IBD Details</h1>
    </div>

    <html:errors/>


    <form action="/web/patient/ibd" name="patientInput" class="js-patient-form" styleClass="form-horizontal">


        <tr>
            <td>
                <b>IBD Details</b>
            </td>
        </tr>

        <tr>
            <td>
                <b>Primary Diagnosis</b>
            </td>
            <td>
                <select name="primaryDiagnosis" class="js-ibd-primary-diagnosis">
                    <option value="-1" selected="selected">Please select</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>
                <b>Disease Extend</b>
            </td>
            <td>
                <select name="diseaseExtent" class="js-ibd-disease-extent">
                    <option value="-1" selected="selected">Please select</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>
                <b>Date of Diagnosis</b>
            </td>
            <td>
                <div class="date datePicker controls">
                    <input name="yearOfDiagnosis" class="js-ibd-diagnosis-year" size="16" type="text">
                    <span class="add-on"><i class="icon-th"></i></span>
                    <span class="help-inline">dd-mm-yyyy</span>
                </div>
            </td>
        </tr>

        <tr>
            <td>
                <b>Complications</b>
            </td>
            <td>
                <select name="complications" class="js-ibd-complications"  multiple="18">
                </select>
            </td>
        </tr>
        <tr>
            <td>
                <b>Extra-intestinal manifestations</b>
            </td>
            <td>
                <select name="manifestations" class="js-ibd-manifestations"  multiple="18">
                </select>
            </td>
        </tr>
        <tr>
            <td>
                <b>Smoking History</b>
            </td>
            <td>
                <select name="smokingHistory" class="js-ibd-smoking-history">
                    <option value="-1" selected="selected">Please select</option>
                </select>

            </td>
        </tr>
        <tr>
            <td>
                <b>Other parts affected</b>
            </td>
            <td>
                <input name="otherPartsAffected" class="js-ibd-parts-affected">
            </td>
        </tr>
        </tr>
        <tr>
            <td>
                <b>Vaccination Record</b>
            </td>
            <td>
                <input name="vaccinationRecord" class="js-ibd-vaccination-record">
            </td>
        </tr>
        <tr>
            <td>
                <b>Colonoscopy surveillance</b>
            </td>
            <td>
                <input name="surveilanceColonoscopy" class="js-ibd-surveillance-colonoscopy">
            </td>
        </tr>
        <tr>
            <td>
                <b>Current Medications</b>
            </td>
            <td>
                <select name="currentMedications" class="js-ibd-current-medications">
                    <option value="-1" selected="selected">None</option>
                </select>

            </td>
        </tr>
        <tr>
            <td>
                <b>IBD Nurses</b>
            </td>
            <td>
                <input name="namedConsultant" class="js-ibd-named-consultant">
            </td>
        </tr>
        <tr>
            <td>
                <b>IBD Consultant</b>
            </td>
            <td>
                <input name="namedConsultant" class="js-ibd-named-consultant">
            </td>
        </tr>
    </form>

    <form action="/" class="js-ibd-surgical-history-input" styleClass="form-horizontal">

        <tr>
            <td>
                <b>Surgical History</b>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <table class="table table-bordered table-striped js-ibd-surgical-history-display">
                    <thead>
                    <tr>
                        <th class="tableheader">Surgery</th>
                        <th class="tableheader">Date</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td class="tablecell">OUch</td>
                        <td class="tablecell">08/09/080</td>
                        <td><button type="submit" styleClass="btn">Delete</button></td>
                    </tr>
                    </tbody>
                </table>
            </td>
        </tr>

        <tr>
            <td>
                <b>Surgery</b>
            </td>
            <td>
                <select name="surgicalHistory" class="js-ibd-surgical-history">
                    <option value="-1" selected="selected">Please select</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>
                <b>Surgery Date</b>
            </td>
            <td>
                <input name="surgicalDate" class="js-ibd-surgical-history-date" size="16" type="text">
                <span class="add-on"><i class="icon-th"></i></span>
                <span class="help-inline">dd-mm-yyyy</span>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <button type="submit" styleClass="btn" class="js-ibd-surgical-history-add">Add Surgery</button>
            </td>
        </tr>

    </form>

    </table>


    </form>

</div>
</div>

<script src="/js/ibd.js" type="text/javascript"></script>

