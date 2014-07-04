<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml/>

<div class="page-header">
    <h1>Type of Comment</h1>
</div>
<p>Please click on the type of thought that you want to submit.</p>
<p>If you would like to return to the previous page, please click the "Sharing Thoughts Home Page" button.</p>

<table>
    <tr><td>To comment on anything that has been good about care you or others have received, click here: </td>
        <td><html:link action="/patient/sharingThoughtsPositive"><html:submit value="Positive Comment" styleClass="btn btn-success btn-success-sharingthoughts formbutton" /></html:link></td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr><td>To comment on anything that has concerned you about care you or others have received, click here: &nbsp;&nbsp;&nbsp;&nbsp;<br/>
        This could be anything that has either <strong>already</strong> caused harm or <strong>could lead to</strong> harm.</td>
        <td><html:link action="/patient/sharingThoughtsNegative"><html:submit value="Quality or Safety Concern" styleClass="btn btn-danger btn-danger-sharingthoughts formbutton" /></html:link></td></tr>
</table>
<br/>
<p><html:link action="/patient/sharingThoughts"><html:submit value="Sharing Thoughts Home Page" styleClass="btn formbutton" /></html:link></p>

