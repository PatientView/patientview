<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

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
        <h1>Unit User</h1>
    </div>

<strong> <span style="color: red">WARNING:</span></strong>

    <logic:present name="userAlreadyExists" >
        There is already a unit user registered on PatientView with the same username or email but in another unit.
    </logic:present>
    <logic:present name="userAlreadyExistsOtherSpecialty" >
        There is already a unit user registered on PatientView with the same username but in another unit in another specialty.
    </logic:present>
    <logic:present name="userAlreadyExistsWithEmail" >
        There is already a unit user registered on PatientView with the same email (<bean:write name="userAlreadyExistsWithEmail" />) but in another unit.
    </logic:present>
    <logic:present name="userAlreadyExistsWithEmailOtherSpecialty" >
        There is already a unit user registered on PatientView with the same email (<bean:write name="userAlreadyExistsWithEmailOtherSpecialty" />) but in another unit in another specialty.
    </logic:present>

    Please carefully check the details of that user, then you can choose to add the existing user to your unit.
    <br /><br />

<table cellpadding="3" >
    <tr>
      <td><b>User Name</b></td>
      <td><bean:write name="usermapping" property="username"/></td>
    </tr>
    <tr>
        <td><b>User's Current Unit Codes</b></td>
        <td><bean:write name="currentUnitCodes" /></td>
    </tr>
    <tr>
      <td><b>Unit Code To Add To</b></td>
      <td><bean:write name="usermapping" property="unitcode" /></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <html:form action="/control/unitAdminAddToUnit">
        <html:hidden name="usermapping" property="username"/>
        <html:hidden name="usermapping" property="unitcode"/>
        <td colspan="2"><input type="button" value="Cancel" onclick="history.back()" class="formbutton">&nbsp;&nbsp;<html:submit value="Add to Unit" styleClass="formbutton" /></td>
      </html:form>
    </tr>
 </table>


<br />

</div>
</div>