<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html>
<body>
<%@ include file="../WEB-INF/util/navbar.jsp" %>
<%@ include file="../WEB-INF/util/message-bar.jsp" %>
Request to register deposit account.
<form action="/bankapp/dispatcher" method="POST">
    <input type="hidden" name="action" value="register-account"/>
    <input type="hidden" name="type" value="DEPOSIT"/>
    <input type="submit" value="Request"/><br/>
</form>
</body>
</html>

