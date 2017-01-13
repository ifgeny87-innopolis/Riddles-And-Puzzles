<%@ page import="ru.rap.common.PageList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    request.setAttribute("ANSWER_URL", PageList.PAGE_RIDDLE_ANSWER);
    request.setAttribute("LIST_URL", PageList.PAGE_RIDDLES);
%>

<t:wrapper>
    <form action="<c:url value='/j_spring_security_logout' />" method="POST" id="logoutForm">
        <input type="hidden"
               name="${_csrf.parameterName}"
               value="${_csrf.token}"/>
    </form>

    <script>
        function formSubmit() {
            document.getElementById("logoutForm").submit();
        }
    </script>

    <c:if test="${pageContext.request.userPrincipal.name != null}">
        <h2>Welcome : ${pageContext.request.userPrincipal.name}
            |
            <a href="javascript:formSubmit()">Logout</a></h2>
    </c:if>

    <h1>Список загадок и ребусов</h1>

    <t:alertText text="${success_message}" type="success"/>
    <t:alertText text="${error_message}"/>

    <c:if test="${not empty riddles}">
        <table class="table">
            <thead>
            <tr>
                <th>Название</th>
                <th>Отгадано</th>
                <th>Создано</th>
                <th>Статус</th>
                <th>Решено</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${riddles}" var="i">
                <tr>
                    <td><a href="${PATH}/${ANSWER_URL}/${i.key.id}">${i.key.title}</a></td>
                    <td>${i.key.getAnswerCount()}</td>
                    <td>${i.key.updated == null ? i.key.created : i.key.updated}</td>
                    <td>${i.value == null ? "Не решено" : "Решено"}</td>
                    <td>${i.value == null ? "" : i.value}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
</t:wrapper>