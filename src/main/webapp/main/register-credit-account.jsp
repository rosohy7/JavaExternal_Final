<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/style.css">
</head>
<body>
<%@ include file="../WEB-INF/util/header.jsp" %>
<%@ include file="../WEB-INF/util/navbar.jsp" %>
<%@ include file="../WEB-INF/util/message-bar.jsp" %>
<fmt:bundle basename="${bundle}" prefix="register-credit.">
    <h2><fmt:message key="title"/></h2>
<form action="/bankapp/dispatcher" method="POST">
    <input type="hidden" name="action" value="register-account"/>
    <input type="hidden" name="type" value="CREDIT"/>
    <fmt:message key="limit"/>: <input type="textbox" name="limit"/><br/>
    <input type="submit" value="<fmt:message key="button"/>"/><br/>
</form>
</fmt:bundle>
</body>
</html>

