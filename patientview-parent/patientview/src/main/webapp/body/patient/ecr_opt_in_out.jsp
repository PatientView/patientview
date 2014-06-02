<%@ page import="org.patientview.utils.LegacySpringUtils" %>
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

<div class="row">
<div class="span12">
    <div class="page-header">
        <h1>Emergency Care Record</h1>
    </div>
    <% if (request.getParameter("success") != null) { %>
        <div class="alert alert-success">Preferences updated</div>
    <% } %>

    <p>The ECR is a new healthcare project that aims to improve patient safety and care by making sure that your key
        details are known by healthcare staff, wherever and whenever you're being treated. It will share details of
        your allergies and medications held on GP practice computer systems. The Emergency Care Summary Record only includes:</p>

    <p><ul><li>your name, date of birth and gender</li>
        <li>address and phone number</li>
        <li>current medication and any known allergies</li></ul>
    </p><br/>

    <%-- ECR opt in prompt (only if not opted out permanently)--%>
    <logic:equal name="user" property="ecrOptOutPermanently" value="false">
        <form action="/<%=LegacySpringUtils.getSecurityUserManager().getLoggedInSpecialty().getContext()%>/patient/ecrOptInOut.do" method="post">
        <logic:equal name="user" property="ecrOptInStatus" value="true">
            <div class="alert alert-success">
                <p>You are currently opted in to retrieve medication details from your Emergency Care Record.</p>
            </div>
            <p><input type="submit" value="Opt Out" name="buttonAction" class="btn"/></p>
        </logic:equal>
        <logic:equal name="user" property="ecrOptInStatus" value="false">
            <div class="alert alert-warning">
                <p>You are currently opted out of retrieving medication details from your Emergency Care Record.</p>
            </div>
            <p><input type="submit" value="Opt In" name="buttonAction" class="btn btn-primary"/>&nbsp;&nbsp;
            <input type="submit" value="Never Ask Me Again" name="buttonAction" class="btn"/></p>
        </logic:equal>
        </form>
    </logic:equal>
    <logic:equal name="user" property="ecrOptOutPermanently" value="true">
        <div class="alert alert-error">
            <p>You are permanently opted out of retrieving medication details from your Emergency Care Record.</p>
        </div>
    </logic:equal>
</div>
    </div>

