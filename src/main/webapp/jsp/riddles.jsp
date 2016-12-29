<%@ page import="ru.rap.common.PageList" %>
<%@ page import="ru.rap.controllers.UserController" %>
<%@ page import="ru.rap.controllers.RiddleController" %>
<%@ page import="ru.rap.libraries.PagerLibrary" %>
<%@ page import="ru.rap.models.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    request.setAttribute("ANSWER_URL", PageList.RIDDLE_ANSWER);
    request.setAttribute("LIST_URL", PageList.RIDDLES);
%>

<t:wrapper>
    <h1>Список загадок и ребусов</h1>

    <t:alert text="${success_message}" type="success"/>
    <t:alert text="${error_message}"/>

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
                    <td><a href="${PATH}${ANSWER_URL}/${i.key.id}">${i.key.title}</a></td>
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