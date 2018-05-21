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
<c:set var="dto" value="${accountList}"/>
<c:set var="accounts" value="${accountList.accounts}"/>
<c:set var="pagination" value="${accountList.pagination}"/>
<c:set var="current" value="${pagination.targetPage}"/>
<c:set var="previous" value="${current-1}"/>
<c:set var="next" value="${current+1}"/>
<c:set var="last" value="${pagination.lastPage}"/>
<fmt:bundle basename="${bundle}" prefix="user-accounts.">
    <h2><fmt:message key="title"/></h2>
    <table border="1px">
        <tr>
            <td>
                <fmt:message key="account-number"/>
            </td>
            <td>
                <fmt:message key="account-type"/>
            </td>
            <td>
                <fmt:message key="balance"/>
            </td>
            <td>
                <fmt:message key="limit"/>
            </td>
            <td>
                <fmt:message key="interest-rate"/>
            </td>
            <td>
                <fmt:message key="accrued-interest"/>
            </td>
            <td>
                <fmt:message key="expires"/>
            </td>
            <td>
                <fmt:message key="holder"/>
            </td>
        </tr>
        <c:forEach var="account" items="${accounts}">
            <tr>

                <td>
                    <a href="/bankapp/dispatcher?action=account-page&account-number=${account.accountNumber}">
                        <c:out value="${account.accountNumber}"/>
                    </a>
                </td>
                <td>
                    <c:out value="${account.type}"/>
                </td>
                <td>
                    <fmt:formatNumber value="${account.accountBalance}" maxFractionDigits="2"/>
                </td>
                <td>
                    <c:if test="${account.type == 'CREDIT'}">
                        <fmt:formatNumber value="${account.creditLimit}" maxFractionDigits="2"/>
                    </c:if>
                </td>
                <td>
                    <fmt:formatNumber value="${account.interestRate}" maxFractionDigits="2"/>%
                </td>
                <td>
                    <fmt:formatNumber value="${account.accruedInterest}" maxFractionDigits="2"/>
                </td>
                <td>
                    <fmt:formatDate value="${account.expires}" type="date"/>
                </td>
                <td>
                    <c:out value="${account.holder}"/>
                </td>
            </tr>
        </c:forEach>
    </table>
    <table>
        <tr>
            <c:if test="${pagination.targetPage > 1}">
                <td>
                    <a href="/bankapp/dispatcher?action=list-accounts&login=${login}&page=1">
                        1
                    </a>
                </td>
            </c:if>
            <c:if test="${previous>2}">
                <td>...</td>
            </c:if>
            <c:if test="${previous>1}">
                <td>
                    <a href="/bankapp/dispatcher?action=list-accounts&login=${login}&page=${previous}">
                        <c:out value="${previous}"/>
                    </a>
                </td>
            </c:if>

            <td>
                <c:out value="${current}"/>
            </td>
            <c:if test="${next<last}">
                <td>
                    <a href="/bankapp/dispatcher?action=list-accounts&login=${login}&page=${next}">
                        <c:out value="${next}"/>
                    </a>
                </td>
            </c:if>
            <c:if test="${next<last-1}">
                <td>...</td>
            </c:if>
            <c:if test="${current<last}">
                <td>
                    <a href="/bankapp/dispatcher?action=list-accounts&login=${login}&page=${last}">
                        <c:out value="${last}"/>
                    </a>
                </td>
            </c:if>
        </tr>
    </table>
</fmt:bundle>
</body>
</html>