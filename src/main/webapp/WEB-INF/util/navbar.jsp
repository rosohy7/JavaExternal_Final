<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:bundle basename="${bundle}" prefix="navbar.">
    <table>
        <tr>

            <td>
                <form action="${contextPath}/main/home.jsp" method="GET">
                    <input type="submit" value="<fmt:message key="home"/>"/>
                </form>
            </td>
        </tr>
    </table>
    <table>
        <tr>
            <c:if test="${sessionScope.role=='USER'}">
                <td>
                    <form action="${contextPath}/main/register-credit-account.jsp" method="GET">
                        <input type="submit" value="<fmt:message key="request-credit"/>"/>
                    </form>
                </td>

            </c:if>
            <c:if test="${sessionScope.role=='USER'}">
                <td>
                    <form action="${contextPath}/main/register-deposit-account.jsp" method="GET">
                        <input type="submit" value="<fmt:message key="request-deposit"/>"/>
                    </form>
                </td>

            </c:if>
            <c:if test="${empty sessionScope.role}">
                <td>
                    <form action="${contextPath}/auth/log-in.jsp" method="GET">
                        <input type="submit" value="<fmt:message key="login"/>"/>
                    </form>
                </td>
            </c:if>
            <c:if test="${'ADMIN'==sessionScope.role}">
                <td>
                    <form action="${contextPath}/admin/get-user-accounts.jsp" method="POST">
                        <input type="submit" value="<fmt:message key="see-user-accounts"/>"/>
                    </form>
                </td>
            </c:if>

            <c:if test="${'ADMIN'==sessionScope.role}">
                <td>
                    <form action="${contextPath}/dispatcher" method="GET">
                        <input type="hidden" name="action" value="list-unconfirmed"/>
                        <input type="hidden" name="page" value="1"/>
                        <input type="submit" value="<fmt:message key="see-unconfirmed"/>"/>
                    </form>
                </td>
            </c:if>

            <c:if test="${empty sessionScope.role}">
                <td>
                    <form action="${contextPath}/auth/sign-up.jsp" method="GET">
                        <input type="submit" value="<fmt:message key="signup"/>"/>
                    </form>
                </td>
            </c:if>

            <c:if test="${'ADMIN' == sessionScope.role}">
                <td>
                    <form action="${contextPath}/admin/deposit.jsp" method="POST">
                        <input type="submit" value="<fmt:message key="deposit"/>"/>
                    </form>
                </td>
            </c:if>

            <c:if test="${'ADMIN' == sessionScope.role}">
                <td>
                    <form action="${contextPath}/admin/withdraw.jsp" method="POST">
                        <input type="submit" value="<fmt:message key="withdraw"/>"/>
                    </form>
                </td>
            </c:if>
            <c:if test="${not empty sessionScope.role}">
                <td>
                    <form action="${contextPath}/dispatcher" method="POST">
                        <input type="hidden" name="action" value="log-out"/>
                        <input type="submit" value="<fmt:message key="logout"/>"/>
                    </form>
                </td>
            </c:if>
        </tr>
    </table>
</fmt:bundle>