<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<body>
<%@ include file="util/navbar.jsp" %>
<%@ include file="util/message-bar.jsp" %>
<c:set var="history" value="${account.history}"/>
<c:set var="transactions" value="${history.transactions}"/>
<c:set var="pagination" value="${history.pagination}"/>
<c:set var="current" value="${pagination.targetPage}"/>
<c:set var="previous" value="${current-1}"/>
<c:set var="next" value="${current+1}"/>
<c:set var="last" value="${pagination.lastPage}"/>
<table border="1px">
    <tr>

        <td>
            <c:out value="${account.accountNumber}"/>
        </td>
        <td>
            <c:out value="${account.type}"/>
        </td>
        <td>
        <c:if test="${account.type == 'CREDIT'}">

                <c:out value="${account.creditLimit}"/>

        </c:if>
        </td>
        <td>
            <c:out value="${account.accountBalance}"/>
        </td>
        <td>
            <c:out value="${account.holder}"/>
        </td>


    </tr>
</table>
<form action="/bankapp/dispatcher" method="POST">
    <input type="hidden" name="action" value="transfer-money"/>
    <input type="hidden" name="from" value="${account.accountNumber}"/>
    Transfer money to this number: <input type="textbox" name="to"/><br/>
    Amount to transfer: <input type="textbox" name="amount"/><br/>
    <input type="submit" value="Transfer"/>
</form>
<h1>History </h1>
<table border="1px">
    <c:forEach var="transaction" items="${transactions}">
        <tr>
            <td>
                <c:out value="${transaction.balanceChange}"/>
            </td>
            <td>
                <c:out value="${transaction.bankFee}"/>
            </td>
            <td>
                <c:out value="${transaction.type}"/>
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
</body>
</html>
