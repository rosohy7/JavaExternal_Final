<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${contextPath}/resources/css/style.css">
</head>
<body>
<%@ include file="../WEB-INF/util/header.jsp" %>
<%@ include file="../WEB-INF/util/navbar.jsp" %>
<%@ include file="../WEB-INF/util/message-bar.jsp" %>
<c:set var="history" value="${account.history}"/>
<c:set var="transactions" value="${history.transactions}"/>
<c:set var="pagination" value="${history.pagination}"/>
<c:set var="current" value="${pagination.targetPage}"/>
<c:set var="previous" value="${current-1}"/>
<c:set var="next" value="${current+1}"/>
<c:set var="last" value="${pagination.lastPage}"/>
<fmt:bundle basename="${bundle}" prefix="account.">
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
                <fmt:message key="limit"/>
            </td>
            <td>
                <fmt:message key="balance"/>
            </td>
            <td>
                <fmt:message key="holder"/>
            </td>


        <tr>

            <td>
                <c:out value="${account.accountNumber}"/>
            </td>
            <td>
                <c:out value="${account.type}"/>
            </td>
            <td>
                <c:if test="${account.type == 'CREDIT'}">
                    <fmt:formatNumber value="${account.creditLimit}" maxFractionDigits="2"/>
                </c:if>
            </td>
            <td>
                <fmt:formatNumber value="${account.accountBalance}" maxFractionDigits="2"/>
            </td>
            <td>
                <c:out value="${account.holder}"/>
            </td>


        </tr>
    </table>
    <c:if test="${role == 'USER'}">
        <h2><fmt:message key="transfer-title"/></h2>
        <form action="/bankapp/dispatcher" method="POST">
            <input type="hidden" name="action" value="transfer-money"/>
            <input type="hidden" name="from" value="${account.accountNumber}"/>
            <fmt:message key="transfer-to"/>: <input type="text" name="to"/><br/>
            <fmt:message key="transfer-amount"/>: <input type="text" name="amount"/><br/>
            <input type="submit" value="<fmt:message key="transfer-button"/>"/>
        </form>
    </c:if>
    <h2><fmt:message key="history"/></h2>
    <table border="1px">
        <tr>
            <td>
                <fmt:message key="balance-change"/>
            </td>
            <td>
                <fmt:message key="fee"/>
            </td>
            <td>
                <fmt:message key="sender-number"/>
            </td>
            <td>
                <fmt:message key="receiver-number"/>
            </td>
            <td>
                <fmt:message key="timestamp"/>
            </td>
        </tr>
        <c:forEach var="transaction" items="${transactions}">
            <tr>
                <td>
                    <fmt:formatNumber value="${transaction.balanceChange}" maxFractionDigits="2"/>
                </td>
                <td>
                    <fmt:formatNumber value="${transaction.bankFee}" maxFractionDigits="2"/>
                </td>
                <td>
                    <c:if test="${transaction.type == 'TRANSFER_TO'}">
                        <c:out value="${transaction.senderNumber}"/>
                    </c:if>
                </td>
                <td>
                    <c:if test="${transaction.type == 'TRANSFER_FROM'}">
                        <c:out value="${transaction.receiverNumber}"/>
                    </c:if>
                </td>
                <td>
                    <fmt:formatDate value="${transaction.timestamp}" type="both"/>

                </td>

            </tr>
        </c:forEach>
    </table>
    <table>
        <tr>
            <c:if test="${pagination.targetPage > 1}">
                <td>
                    <a href="/bankapp/dispatcher?action=account-page&account-number=${account.accountNumber}&page=1">
                        1
                    </a>
                </td>
            </c:if>
            <c:if test="${previous>2}">
                <td>...</td>
            </c:if>
            <c:if test="${previous>1}">
                <td>
                    <a href="/bankapp/dispatcher?action=account-page&account-number=${account.accountNumber}&page=${previous}">
                        <c:out value="${previous}"/>
                    </a>
                </td>
            </c:if>

            <td>
                <c:out value="${current}"/>
            </td>
            <c:if test="${next<last}">
                <td>
                    <a href="/bankapp/dispatcher?action=account-page&account-number=${account.accountNumber}&page=${next}">
                        <c:out value="${next}"/>
                    </a>
                </td>
            </c:if>
            <c:if test="${next<last-1}">
                <td>...</td>
            </c:if>
            <c:if test="${current<last}">
                <td>
                    <a href="/bankapp/dispatcher?action=account-page&account-number=${account.accountNumber}&page=${last}">
                        <c:out value="${last}"/>
                    </a>
                </td>
            </c:if>
        </tr>
    </table>
</fmt:bundle>
</body>
</html>
