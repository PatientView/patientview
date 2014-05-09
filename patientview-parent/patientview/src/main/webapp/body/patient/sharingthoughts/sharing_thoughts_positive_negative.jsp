<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml/>

<div class="page-header">
    <h1>Type of comment</h1>
</div>

<br /><br/>

<p align="center">
    <html:link action="/patient/sharingThoughtsPositive"><html:submit value="Positive Comment" styleClass="btn btn-success btn-success-sharingthoughts formbutton" /></html:link>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <html:link action="/patient/sharingThoughtsNegative"><html:submit value="Quality or Safety Concern" styleClass="btn btn-danger btn-danger-sharingthoughts formbutton" /></html:link>
</p>