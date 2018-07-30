<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${contextPath}/resources/css/style.css"/>
</head>
<body>
<%@ include file="./WEB-INF/util/header.jsp" %>
<%@ include file="./WEB-INF/util/navbar.jsp" %>
<%@ include file="./WEB-INF/util/message-bar.jsp" %>
<fmt:bundle basename="${bundle}" prefix="index.">
    <h2><fmt:message key="title"/></h2>
</fmt:bundle>
Hello!
</body>
</html>
