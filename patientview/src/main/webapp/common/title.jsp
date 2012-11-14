<%@ page import="com.worthsoln.utils.LegacySpringUtils" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<div class="navbar">
    <div class="navbar-inner">
      <div class="container">
        <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </a>
        <html:link action="/index" styleClass="brand">
            Providing up-to-date medical information for patients and healthcare professionals
            <%--<logic:present specialty="ibd">My IBD</logic:present>
            <logic:present specialty="renal">My RPV</logic:present>--%>
        </html:link>
        <div class="nav-collapse">
            <ul class="nav pull-right">
                <%
                if (LegacySpringUtils.getSecurityUserManager().isLoggedIn()) {
                %>
                    <li class="pull-right "><div class="navText">Logged in as: <b><%= LegacySpringUtils.getSecurityUserManager().getLoggedInUsername()%></b></div></li>
                    <logic:present role="superadmin,unitadmin,unitstaff">
                        <li><html:link action="logged_in">Back to Admin Area</html:link></li>
                    </logic:present>
                    <li><html:link action="logout">Logout</html:link></li>
                    <jsp:include page="include/specialty_switcher.jsp"/>
                <%
                }
                %>
                <logic:present specialty="ibd">
                    <li><html:link action="/ibd-help" styleClass="<%= ("help".equals(request.getAttribute("currentNav"))) ? "navlinkon" : "navlink" %>">Need help <i class="icon-question-sign icon-white"></i></html:link></li>
                </logic:present>
                <logic:present specialty="renal">
                    <li><html:link action="/help" styleClass="<%= ("help".equals(request.getAttribute("currentNav"))) ? "navlinkon" : "navlink" %>">Need help <i class="icon-question-sign icon-white"></i></html:link></li>
                </logic:present>
            </ul>

            <%
                if (!LegacySpringUtils.getSecurityUserManager().isLoggedIn()) {
            %>
                <form class="navbar-form pull-right" action="j_security_check" method="POST">
                    <input type="text" class="span2" name="j_username" placeholder="Username">
                    <input type="password" class="span2" name="j_password" placeholder="Password">
                    <input class="btn" type="submit" value="Login">
                </form>
            <%
                }
            %>
        </div><!-- /.nav-collapse -->
      </div>
    </div><!-- /navbar-inner -->
</div>

<div class="container">