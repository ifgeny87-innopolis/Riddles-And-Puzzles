<%@ page import="ru.rap.common.PageList" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = (String) request.getAttribute("PATH");
    request.setAttribute("LIST_URL", path + "/" + PageList.PAGE_RIDDLES);
%>
<t:wrapper>

    <h1>${title}</h1>

    <t:alertText text="${wrong_message}" type="warning"/>

    <t:alertText text="${error_message}"/>

    <c:choose>
        <c:when test="${text != null}">
            <blockquote>
                    ${text}
            </blockquote>

            <form method="post">
                <div class="form-group">
                    <label for="answer">Введите ответ</label>
                    <input type="text" name="answer" value="${answer}" class="form-control" id="answer"
                           placeholder="Ответ на загадку"/>
                </div>

                <button type="button" class="btn btn-default" onclick="location.href='${LIST_URL}';">Отмена</button>
                <button type="submit" class="btn btn-success">Отправить</button>
            </form>
        </c:when>

        <c:otherwise>
            <button type="button" class="btn btn-default" onclick="location.href='${LIST_URL}';">Назад</button>
        </c:otherwise>
    </c:choose>
</t:wrapper>