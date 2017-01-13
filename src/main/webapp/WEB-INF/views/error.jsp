<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%
    request.setAttribute("PATH", request.getContextPath());

    // Проверяю, правильно ли указали код и номер ошибки
    Object code = request.getAttribute("error_code");
    if (code != null) {
        response.setStatus(new Integer(code.toString()));
    }
    String code_text = (code == null || !(code instanceof Integer))
            ? "Произошла неизвестная ошибка"
            : "Была вызвана ошибка #" + code.toString();
    request.setAttribute("code_text", code_text);
%>

<t:wrapper>
    <h1>${requestScope.code_text}</h1>

    <t:alertText text="${error_message}"/>

    <t:alertException/>

    <p>
        Мы уже влотную занялись этой ошибкой. Но вы все равно можете принять участие и сообщить нам об ошибке по
        телефону 8-800-55-ERROR.
    </p>

    <p>
        По <a href="${PATH}">этой ссылке</a> вы можете перейти на главную страницу.
    </p>

    <p>
        Попробуйте <b>это</b> и вот <i>это</i>. Возможно, это вам поможет.
    </p>

    <p>
        <i>Хорошего Вам дня!</i>
    </p>
</t:wrapper>

