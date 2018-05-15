<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html>
<body>
<%@ include file="../WEB-INF/util/navbar.jsp" %>
<%@ include file="../WEB-INF/util/message-bar.jsp" %>
<form action="/bankapp/dispatcher" method="POST">
    <input type="hidden" name="action" value="register-account"/>
    <input type="hidden" name="type" value="CREDIT"/>

    Credit limit: <input type="textbox" name="limit"/><br/>
    <input type="submit" value="Request"/><br/>
</form>
</body>
</html>

