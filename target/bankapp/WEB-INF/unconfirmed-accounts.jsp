<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<body>
<%@ include file="util/navbar.jsp" %>
<%@ include file="util/message-bar.jsp" %>
<c:set var="dto" value="${accountList}"/>
<c:set var="accounts" value="${accountList.accounts}"/>
<c:set var="pagination" value="${accountList.pagination}"/>
<c:set var="current" value="${pagination.targetPage}"/>
<c:set var="previous" value="${current-1}"/>
<c:set var="next" value="${current+1}"/>
<c:set var="last" value="${pagination.lastPage}"/>
<table border="1px">
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
            <c:if test="${account.type == 'CREDIT'}">
                <td>
                    <c:out value="${account.creditLimit}"/>
                </td>
            </c:if>
            <c:if test="${not empty sessionScope.role}">
                <td>
                    <form action="/bankapp/dispatcher" method="POST">
                        <input type="hidden" name="action" value="confirm-account"/>
                        <input type="hidden" name="account-number" value="${account.accountNumber}"/>
                        <input type="submit" value="Confirm"/><br/>
                    </form>
            </td>
            </c:if>

        </tr>
    </c:forEach>
</table>
<table>
    <tr>
        <c:if test="${pagination.targetPage > 1}">
            <td>
                <a href="/bankapp/dispatcher?action=list-unconfirmed&page=1">
                    1
                </a>
            </td>
        </c:if>
        <c:if test="${previous>2}">
            <td>...</td>
        </c:if>
        <c:if test="${previous>1}">
            <td>
                <a href="/bankapp/dispatcher?action=list-unconfirmed&page=${previous}">
                    <c:out value="${previous}"/>
                </a>
            </td>
        </c:if>

        <td>
            <c:out value="${current}"/>
        </td>
        <c:if test="${next<last}">
            <td>
                <a href="/bankapp/dispatcher?action=list-unconfirmed&page=${next}">
                    <c:out value="${next}"/>
                </a>
            </td>
        </c:if>
        <c:if test="${next<last-1}">
            <td>...</td>
        </c:if>
        <c:if test="${current<last}">
            <td>
                <a href="/bankapp/dispatcher?action=list-unconfirmed&page=${last}">
                    <c:out value="${last}"/>
                </a>
            </td>
        </c:if>
    </tr>
</table>
</body>
</html>