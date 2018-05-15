<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${not empty message}">
    <p style="background-color:salmon">
        <c:out value="${message}"/>
        <c:remove var="message" scope="session"/>
    </p>
</c:if>