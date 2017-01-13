<%@ page import="ru.rap.common.PageList" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%
    String path = (String) request.getAttribute("PATH");
    request.setAttribute("LIST_URL", path + PageList.PAGE_RIDDLE_MINE);
%>

<t:wrapper>

    <t:alertText text="${error_message}"/>

    <h1>Создание новой загадки</h1>
    <form method="post">
        <div class="form-group">
            <label for="title">Название загадки</label>
            <input type="text" name="title" value="${title}" class="form-control" id="title" placeholder="Название загадки"/>
        </div>

        <div class="form-group">
            <label for="text">Текст загадки</label>
            <textarea name="text" class="form-control" id="text" placeholder="Текст загадки">${text}</textarea>
        </div>

        <div class="form-group">
            <label for="image">Изображение</label>
            <img/>
            <input type="file" name="image" class="form-control" id="image"/>
        </div>

        <div class="form-group">
            <label for="answers">Ответ на загадку</label>
            <input type="text" name="answer" value="${answer_text}" class="form-control" id="answers" placeholder="Ответ на загадку"/>
            <blockquote>
                Если правильных ответов на загадку несколько, укажите их через запятую.<br/>
                При сохранении ответов будут проигнорированы: регистр букв, двойные пробелы и пробелы на концах.
            </blockquote>
        </div>

        <button type="button" class="btn btn-default" onclick="location.href='${LIST_URL}';">Отмена</button>
        <button type="submit" class="btn btn-success">Создать</button>
    </form>
</t:wrapper>