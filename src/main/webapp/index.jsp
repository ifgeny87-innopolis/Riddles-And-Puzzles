<%@ page import="ru.rap.controllers.UserController" %>
<%@ page import="ru.rap.common.PageList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%
    // Проверка авторизации
    if(UserController.isUserAuth(request.getRequestedSessionId())) {
        request.getRequestDispatcher(PageList.RIDDLES).forward(request, response);
    }

    // ни один контроллер не вызывается перед этой страницей
    // поэтому надо назначить PATH
    String path = request.getContextPath();
    request.setAttribute("PATH", path);

    request.setAttribute("REGISTER_URL", path + PageList.REGISTER);
    request.setAttribute("AUTH_URL", path + PageList.AUTH);
%>

<t:wrapper>
    <h1>Добро пожаловать в систему управления ребусами и загадками</h1>

    <t:alert text="${requestScope.register_err}"/>

    <div class="row">

        <div class="col-md-6">
            <form method="post" action="${REGISTER_URL}">
                <h3>Регистрация</h3>
                <div class="form-group">
                    <label for="regEmail">Email</label>
                    <input type="text" name="regEmail" class="form-control" id="regEmail" placeholder="Email">
                </div>
                <div class="form-group">
                    <label for="regPwd">Пароль</label>
                    <input type="password" name="regPwd" class="form-control" id="regPwd" placeholder="Пароль">
                </div>
                <button type="submit" class="btn btn-default">Отправить</button>
            </form>
        </div>

        <div class="col-md-6">
            <form method="post" action="${AUTH_URL}">
                <h3>Авторизация</h3>
                <div class="form-group">
                    <label for="authEmail">Email</label>
                    <input type="text" name="authEmail" class="form-control" id="authEmail" placeholder="Email">
                </div>
                <div class="form-group">
                    <label for="authPwd">Пароль</label>
                    <input type="password" name="authPwd" class="form-control" id="authPwd" placeholder="Пароль">
                </div>
                <button type="submit" class="btn btn-default">Вход</button>
            </form>
        </div>

    </div>
</t:wrapper>

