<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%
    request.setAttribute("PATH", request.getContextPath());

    // Проверяю, правильно ли указали код и номер ошибки
    Object message = request.getAttribute("error_message");
    if(message == null || message == "" || !(message instanceof String)) {
    	message = "";
    }
    request.setAttribute("error_message1", message);

    Object code = request.getAttribute("error_code");
    message = (code == null || !(code instanceof Integer))
            ? "Произошла неизвестная ошибка"
            : "Была вызвана ошибка #" + code.toString();
    request.setAttribute("error_code1", message);
%>

<t:wrapper>
    <h1>${requestScope.error_code1}</h1>

    <t:alert text="${requestScope.error_message1}"/>

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

