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
    <table class="table table-bordered table-striped" id="tableSharedThoughts">
        <thead>
        <tr>
            <th style="padding-right: 80px;">Type</th>
            <th>Start Date</th>
            <th>End Date</th>
            <th>Location</th>
            <th>Description</th>
            <th>Messages</th>
            <th>Submitted</th>
            <th>Updated</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <logic:iterate name="sharedThoughts" id="thought">
        <tr>
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
            <td class="tablecell"><bean:write name="thought" property="descriptionBeginning"/></td>
            <logic:present name="thought" property="conversation">
                <td class="tablecell">
                    <bean:size id="messageCount" name="thought" property="conversation.messages"/>
                    <bean:write name="messageCount"/>
                </td>
            </logic:present>
            <logic:notPresent name="thought" property="conversation">
                <td class="tablecell">0</td>
            </logic:notPresent>
            <td class="tablecell"><bean:write name="thought" property="submitDateFormattedDateTime"/></td>
            <td class="tablecell"><bean:write name="thought" property="dateLastSavedFormattedDateTime"/></td>
            <logic:present role="unitstaff,unitadmin,superadmin">
                <td class="tablecell"><html:form action="/control/sharingThoughtsViewThought"><html:hidden name="thought" property="id"/><html:submit value="View" styleClass="btn" /></html:form></td>
            </logic:present>
        </tr>
        </logic:iterate>
        </tbody>
    </table>

    <script type="text/javascript" src="/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript">
        $('#tableSharedThoughts').dataTable({
            "bSort" : false,
            "iDisplayLength": 10
        });
    </script>

</logic:notEmpty>
</div>