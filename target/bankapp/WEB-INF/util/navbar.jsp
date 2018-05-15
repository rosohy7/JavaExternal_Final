<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="context" value="${pageContext.request.contextPath}"/>
<table border="1px">
    <tr>

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