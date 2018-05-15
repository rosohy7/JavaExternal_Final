<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="context" value="${pageContext.request.contextPath}"/>
<table border="1px">
    <tr>
        <c:if test="${sessionScope.role=='USER'}">
            <td>
                <form action="${context}/main/register-credit-account.jsp" method="GET">
                    <input type="submit" value="Register Account" />
                </form>
            </td>
        </c:if>
        <c:if test="${empty sessionScope.role}">
            <td>
                <form action="${context}/auth/log-in.jsp" method="GET">
                    <input type="submit" value="Log In" />
                </form>
            </td>
        </c:if>

        <c:if test="${not empty sessionScope.role}">
            <td>
                <form action="${context}/dispatcher" method="POST">
                    <input type="hidden" name="action" value="log-out"/>
                    <input type="submit" value="Log Out" />
                </form>
            </td>
        </c:if>




    </tr>
</table>