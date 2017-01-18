<%@ page import="ru.rap.common.PageList" %>
<%@ page import="ru.rap.controllers.UserController" %>
<%@ page import="ru.rap.controllers.RiddleController" %>
<%@ page import="ru.rap.libraries.PagerLibrary" %>
<%@ page import="ru.rap.models.UserModel" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String path = (String) request.getAttribute("PATH");
    request.setAttribute("CREATE_URL", path + PageList.PAGE_RIDDLE_CREATE);
    request.setAttribute("EDIT_URL", path + PageList.PAGE_RIDDLE_EDIT);
    request.setAttribute("LIST_URL", path + PageList.PAGE_RIDDLE_MINE);
%>

<t:wrapper>
    <h1>Мои загадки и ребусы</h1>

    <div>
        <a class="btn btn-default" href="${CREATE_URL}">Создать</a>
    </div>

    <t:alertText text="${success_message}" type="success"/>
    <t:alertText text="${error_message}"/>

    <c:if test="${not empty riddles}">
        <table class="table">
            <thead>
            <tr>
                <th>Название</th>
                <th>Отгадано</th>
                <th>Создано</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${riddles}" var="i">
                <tr>
                    <td><a href="${EDIT_URL}/${i.id}">${i.title}</a></td>
                    <td>${i.getAnswerCount()}</td>
                    <td>${i.updated == null ? i.created : i.updated}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
</t:wrapper>