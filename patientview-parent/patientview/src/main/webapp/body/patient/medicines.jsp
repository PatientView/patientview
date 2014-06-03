<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<%--
  ~ PatientView
  ~
  ~ Copyright (c) Worth Solutions Limited 2004-2013
  ~
  ~ This file is part of PatientView.
  ~
  ~ PatientView is free software: you can redistribute it and/or modify it under the terms of the
  ~ GNU General Public License as published by the Free Software Foundation, either version 3 of the License,
  ~ or (at your option) any later version.
  ~ PatientView is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
  ~ the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  ~ GNU General Public License for more details.
  ~ You should have received a copy of the GNU General Public License along with PatientView in a file
  ~ titled COPYING. If not, see <http://www.gnu.org/licenses/>.
  ~
  ~ @package PatientView
  ~ @link http://www.patientview.org
  ~ @author PatientView <info@patientview.org>
  ~ @copyright Copyright (c) 2004-2013, Worth Solutions Limited
  ~ @license http://www.gnu.org/licenses/gpl-3.0.html The GNU General Public License V3.0
  --%>

<html:xhtml/>

<div class="page-header">
    <h1>Medicines</h1>
</div>

<%-- ECR opt in prompt (only if not opted out permanently)--%>
<logic:equal name="ecrEnabled" value="true">
    <logic:equal name="user" property="ecrOptOutPermanently" value="false">
        <logic:equal name="user" property="ecrOptInStatus" value="true">
            <div class="alert alert-success">
                <div class="row">
                    <div class="span8">
                        <h2>Emergency Care Record</h2>
                        <p>You are currently opted in to retrieve medication details from your Emergency Care Record.</p>
                    </div>
                    <div class="span3">
                        <br/>
                        <p><html:link action="/patient/ecrOptInOut" styleClass="btn">More Information</html:link>&nbsp;&nbsp;
                            <html:link action="/patient/ecrOptInOut" styleClass="btn">Opt Out</html:link></p>
                    </div>
                </div>
            </div>
        </logic:equal>
        <logic:notEqual name="user" property="ecrOptInStatus" value="true">
            <div class="alert alert-warning">
                <h2>Emergency Care Record</h2>
                <p>You are currently opted out of retrieving medication details from your Emergency Care Record.</p>
                <p><html:link action="/patient/ecrOptInOut" styleClass="btn btn-primary">Opt In</html:link>&nbsp;&nbsp;
                    <html:link action="/patient/ecrOptInOut" styleClass="btn">Never Ask Me Again</html:link></p>
            </div>
        </logic:notEqual>
    </logic:equal>
</logic:equal>

<p><bean:message key="cautionary.medicines" /></p>
<p><bean:message key="link.medicines" /></p>

<%-- Medication (not ECR) --%>
<logic:notEmpty name="medicines">
    <h2 class="tableheader" colspan="4">Medicines for <bean:write name="user" property="name"/></h2>
    <div id="medications">
        <h3>From Your Units</h3>
        <table width="650" border="0" cellspacing="1" cellpadding="3" class="table table-bordered table-striped">
            <thead>
            <tr>
                <th class="tablecellbold" width="75"><b>Start Date</b></th>
                <th class="tablecellbold">Medicine Name</th>
                <th class="tablecellbold">Dose</th>
                <th class="tablecellbold">Source</th>
            </tr>
            </thead>
            <tbody>
            <logic:iterate name="medicines" id="medicine">
                <tr>
                    <td class="tablecell"><bean:write name="medicine" property="formattedStartDate"/></td>
                    <td class="tablecell"><bean:write name="medicine" property="name"/></td>
                    <td class="tablecell"><bean:write name="medicine" property="dose"/></td>
                    <td class="tablecell"><bean:write name="medicine" property="shortname"/></td>
                </tr>
            </logic:iterate>
            </tbody>
        </table>
    </div>
</logic:notEmpty>
<logic:empty name="medicines">
    <div class="alert">No medicines uploaded</div>
</logic:empty>

<%-- Medication (ECR) --%>
<logic:equal name="ecrEnabled" value="true">
    <logic:equal name="user" property="ecrOptOutPermanently" value="false">
        <logic:equal name="user" property="ecrOptInStatus" value="true">
            <logic:notEmpty name="medicinesECR">
                <div id="medications-ecr">
                    <h3>From Your Emergency Care Record</h3>
                    <table width="650" border="0" cellspacing="1" cellpadding="3" class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th class="tablecellbold" width="75"><b>Start Date</b></th>
                            <th class="tablecellbold">Medicine Name</th>
                            <th class="tablecellbold">Dose</th>
                        </tr>
                        </thead>
                        <tbody>
                        <logic:iterate name="medicinesECR" id="medicine">
                            <tr>
                                <td class="tablecell"><bean:write name="medicine" property="formattedStartDate"/></td>
                                <td class="tablecell"><bean:write name="medicine" property="name"/></td>
                                <td class="tablecell"><bean:write name="medicine" property="dose"/></td>
                            </tr>
                        </logic:iterate>
                        </tbody>
                    </table>
                </div>
            </logic:notEmpty>
            <logic:empty name="medicinesECR">
                <div class="alert">No ECS medicines uploaded</div>
            </logic:empty>
        </logic:equal>
    </logic:equal>
</logic:equal>