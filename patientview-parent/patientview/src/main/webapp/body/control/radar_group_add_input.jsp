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
<div class="span9">
<div class="page-header">
    <h1>RaDaR Group Add</h1>
</div>

<html:errors />

<html:form action="/control/radarGroupAdd">

    <logic:present name="UnitExisted">
        <bean:message key="errors.header"/><bean:message key="errors.prefix"/><bean:message
            key="unitcode.duplicated"/><bean:message key="errors.suffix"/><bean:message key="errors.footer"/>
    </logic:present>

  <table cellpadding="3" >

    <tr>
      <td><img src="images/space.gif" height="10" /> </td>
    </tr>
    <tr>
      <td><b>Code</b></td>
      <td><html:text property="unitcode" /></td>
    </tr>

    <tr>
      <td><b>Name</b></td>
      <td><html:text property="name" /></td>
    </tr>

    <tr>
      <td><b>Short Name</b></td>
      <td><html:text property="shortname" maxlength="15"/></td>
    </tr>

    <html:hidden property="sourceType" value="radargroup"/>

    <tr>
      <td><b>Admin Name</b></td>
      <td><html:text property="renaladminname" /></td>
    </tr>

    <tr>
      <td><b>Admin Phone</b></td>
      <td><html:text property="renaladminphone" /></td>
    </tr>

    <tr>
      <td><b>Admin Email</b></td>
      <td><html:text property="renaladminemail" /></td>
    </tr>

  </table>

    <h2>Disease Group Specific Information</h2>
    <br/>
    <table cellpadding="3" class="table table-bordered table-striped">
        <tr>
            <td></td>
            <td align="center"><b>Link (e.g. http:// etc.)</b></td>
            <td align="center"><b>Text Description</b></td>
        </tr>
        <tr>
            <td><b>Medical Link 1</b></td>
            <td><html:text property="medicalLink01" /></td>
            <td><html:text property="medicalLinkText01" /></td>
        </tr>
        <tr>
            <td><b>Medical Link 2</b></td>
            <td><html:text property="medicalLink02" /></td>
            <td><html:text property="medicalLinkText02" /></td>
        </tr>
        <tr>
            <td><b>Medical Link 3</b></td>
            <td><html:text property="medicalLink03" /></td>
            <td><html:text property="medicalLinkText03" /></td>
        </tr>
        <tr>
            <td><b>Medical Link 4</b></td>
            <td><html:text property="medicalLink04" /></td>
            <td><html:text property="medicalLinkText04" /></td>
        </tr>
        <tr>
            <td><b>Medical Link 5</b></td>
            <td><html:text property="medicalLink05" /></td>
            <td><html:text property="medicalLinkText05" /></td>
        </tr>
        <tr>
            <td><b>Medical Link 6</b></td>
            <td><html:text property="medicalLink06" /></td>
            <td><html:text property="medicalLinkText06" /></td>
        </tr>
        <tr>
            <td><b>Patient Link 1</b></td>
            <td><html:text property="patientLink01" /></td>
            <td><html:text property="patientLinkText01" /></td>
        </tr>
        <tr>
            <td><b>Patient Link 2</b></td>
            <td><html:text property="patientLink02" /></td>
            <td><html:text property="patientLinkText02" /></td>
        </tr>
        <tr>
            <td><b>Patient Link 3</b></td>
            <td><html:text property="patientLink03" /></td>
            <td><html:text property="patientLinkText03" /></td>
        </tr>
        <tr>
            <td><b>Patient Link 4</b></td>
            <td><html:text property="patientLink04" /></td>
            <td><html:text property="patientLinkText04" /></td>
        </tr>
        <tr>
            <td><b>Patient Link 5</b></td>
            <td><html:text property="patientLink05" /></td>
            <td><html:text property="patientLinkText05" /></td>
        </tr>
        <tr>
            <td><b>Patient Link 6</b></td>
            <td><html:text property="patientLink06" /></td>
            <td><html:text property="patientLinkText06" /></td>
        </tr>
    </table>

    <html:submit value="Add" styleClass="btn"/>

</html:form>
</div>

</div>
