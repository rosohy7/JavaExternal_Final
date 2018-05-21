<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isErrorPage="true" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<html>
<head>
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/style.css">
</head>
<body>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<%@ include file="../WEB-INF/util/header.jsp" %>
<%@ include file="../WEB-INF/util/navbar.jsp" %>
<%@ include file="../WEB-INF/util/message-bar.jsp" %>
An expected error has happened.
<a href="/bankapp/">Main page</a>
</body>
</html>
