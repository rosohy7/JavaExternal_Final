<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html>
<body>
<h2> Log In Form </h2>
<%@ include file="../WEB-INF/util/navbar.jsp" %>
<%@ include file="../WEB-INF/util/message-bar.jsp" %>
<form action="/bankapp/dispatcher" method="POST">
<input type="hidden" name="action" value="log-in"/>
Login name: <input type="textbox" name="login"/><br/>
Password: <input type="password" name="password"/><br/>
<input type="submit" value="Sign Up"/><br/>
</form>
</body>
</html>