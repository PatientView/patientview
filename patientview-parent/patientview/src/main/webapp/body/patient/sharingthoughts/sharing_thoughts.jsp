<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml/>

<div class="page-header">
    <h1>Sharing thoughts about my care</h1><br /><h3>The renal patient anonymous quality and safety feedback system</h3>
</div>
<div>
    <p>
        Please tell us about the care that you or others have received in your renal service - things that have been good
        or things that have concerned you. We want to hear about this so that we can improve your care.
    </p>
    <p>
        If you believe that anything requires urgent action or may be life-threatening do tell us but you <strong>must also
        tell a staff member straight away.</strong>
    </p>
    <p>
        If you tell us about your care or you receive a message on RPV about your comments, staff from your renal unit and
        the research team will not know who you are unless you choose to give your name
    </p>
    <table>
        <tr><td>To start a new comment, click here: </td><td><html:link action="/patient/sharingThoughtsPositiveNegative"><html:submit value="Share a New Thought" styleClass="btn btn-primary btn-primary-sharingthoughts formbutton" /></html:link></td></tr>
        <tr><td>To see thoughts you've submitted or saved, click here: &nbsp; &nbsp; &nbsp;</td><td><html:link action="/patient/sharingThoughtsUserList"><html:submit value="Your Thoughts" styleClass="btn formbutton btn-sharingthoughts " /></html:link></td></tr>
        <tr><td>To see monthly feedback from your unit, click here: </td><td><html:link action="/patient/sharingThoughtsSummary"><html:submit value="Monthly Feedback Summary" styleClass="btn formbutton btn-sharingthoughts " /></html:link></td></tr>
    </table>
    <p>The information you provide will be looked at by staff from your renal unit, but please be aware that some concerns may take time to solve.</p>
</div>