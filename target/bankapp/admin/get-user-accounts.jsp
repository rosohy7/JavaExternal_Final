<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html>
<body>
<h2> Log In Form </h2>
<%@ include file="../WEB-INF/util/navbar.jsp" %>
<%@ include file="../WEB-INF/util/message-bar.jsp" %>
<form action="/bankapp/dispatcher" method="GET">
    <input type="hidden" name="action" value="list-accounts"/>
    Login name: <input type="textbox" name="login"/><br/>
    <input type="submit" value="Sign Up"/><br/>
</form>
</body>
</html>