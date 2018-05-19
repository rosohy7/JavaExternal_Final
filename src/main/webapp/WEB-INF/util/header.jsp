<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:if test="${empty contentLanguage}">
    <c:set var="contentLanguage" scope="session" value="en"/>
    <c:set var="bundle" scope="session" value="external.letiuka.modelviewcontroller.view.content.EnContent"/>
</c:if>
<table>
    <tr>
       <c:if test="${contentLanguage != 'en'}">
           <td>
               <form action="${contextPath}/dispatcher" method="POST">
                   <input type="hidden" name="action" value="set-language"/>
                   <input type="hidden" name="lang" value="en"/>
                   <input type="submit" value="EN"/>
               </form>
           </td>
       </c:if>
        <c:if test="${contentLanguage != 'ru'}">
            <td>
                <form action="${contextPath}/dispatcher" method="POST">
                    <input type="hidden" name="action" value="set-language"/>
                    <input type="hidden" name="lang" value="ru"/>
                    <input type="submit" value="RU"/>
                </form>
            </td>
        </c:if>
    </tr>
</table>