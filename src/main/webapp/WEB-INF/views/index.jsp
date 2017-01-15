<%@ page import="ru.rap.controllers.UserController" %>
<%@ page import="ru.rap.common.PageList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String path = request.getContextPath() + "/";
    request.setAttribute("PATH", path);
    request.setAttribute("REGISTER_URL", path + PageList.PAGE_REGISTER);
%>

<t:wrapper>
    <h1>Добро пожаловать в систему управления ребусами и загадками</h1>

    <c:if test="${not empty error}">
        <t:alertText text="${error}"/>
    </c:if>

    <c:if test="${not empty msg}">
        <t:alertText text="Soobshenie: ${msg}"/>
    </c:if>

    <div class="row">

        <!-- Форма регистрации -->
        <div class="col-md-6">
            <form method="post" action="${REGISTER_URL}">
                <h3>Регистрация</h3>
                <div class="form-group">
                    <label for="regName">Пользователь</label>
                    <input type="text" name="regName" class="form-control" id="regName">
                </div>
                <div class="form-group">
                    <label for="regPwd">Пароль</label>
                    <input type="password" name="regPwd" class="form-control" id="regPwd" placeholder="Пароль">
                </div>
                <button type="submit" class="btn btn-default">Отправить</button>
            </form>
        </div>

        <!-- Форма авторизации -->
        <div class="col-md-6">
            <form name="loginForm" method="post" action="<c:url value='/j_spring_security_check' />">
                <h3>Авторизация</h3>
                <div class="form-group">
                    <label for="authName">Пользователь</label>
                    <input type="text" name="authName" class="form-control" id="authName">
                </div>
                <div class="form-group">
                    <label for="authPwd">Пароль</label>
                    <input type="password" name="authPwd" class="form-control" id="authPwd" placeholder="Пароль">
                </div>
                <button type="submit" class="btn btn-default">Вход</button>
            </form>
        </div>

    </div>

    <script>
        window.onload = () => { document.loginForm.authName.focus(); };
    </script>
</t:wrapper>