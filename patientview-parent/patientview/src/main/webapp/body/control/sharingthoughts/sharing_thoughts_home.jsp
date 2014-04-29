<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml/>
<div class="span9">
    <div  class="page-header">
        <h1>Sharing Thoughts Admin Home</h1>
    </div>
<logic:empty name="sharedThoughts">
    <div class="alert">No Shared Thoughts</div>
</logic:empty>

<logic:notEmpty name="sharedThoughts">
    <table class="table table-bordered table-striped">
        <thead>
        <tr>
            <th>Date Updated</th>
            <th>Positive/Negative</th>
            <th>Start Date</th>
            <th>End Date</th>
            <th>Location</th>
            <th>Description</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <logic:iterate name="sharedThoughts" id="thought">
        <tr>
            <td class="tablecell"><bean:write name="thought" property="dateLastSavedFormattedDate"/></td>
            <td class="tablecell">
                <logic:equal value="1" name="thought" property="positiveNegative">
                    <span class="yesTick">&#10004; Positive</span>
                </logic:equal>
                <logic:notEqual value="1" name="thought" property="positiveNegative">
                    <span class="noCross">&#10008; Negative</span>
                </logic:notEqual>
            </td>
            <td class="tablecell"><bean:write name="thought" property="startDateFormattedDate"/></td>
            <td class="tablecell"><bean:write name="thought" property="endDateFormattedDate"/></td>
            <td class="tablecell"><bean:write name="thought" property="location"/></td>
            <td class="tablecell"><bean:write name="thought" property="location"/></td>
            <logic:present role="unitstaff,unitadmin,superadmin">
                <td class="tablecell"><html:form action="/control/sharingThoughtsViewThought"><html:hidden name="thought" property="id"/><html:submit value="View/Respond" styleClass="btn" /></html:form></td>
            </logic:present>
        </tr>
        </logic:iterate>
        </tbody>
    </table>
</logic:notEmpty>
</div>