<%@ tag import="ru.rap.models.UserModel" %>
<%@ tag import="ru.rap.common.PageList" %>
<%@ tag description="Simple Wrapper Tag" pageEncoding="UTF-8" %>
<%
    request.setAttribute("EXIT_URL", PageList.PAGE_EXIT);
%>
<!doctype html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Система управления загадками и ребусами</title>

    <script src="${PATH}/app/js/jquery-3.1.1.min.js"></script>
    <script src="${PATH}/app/js/bootstrap.min.js"></script>

    <link rel="stylesheet" href="${PATH}/app/style/bootstrap.min.css">
</head>
<body>
<%
    UserModel user = (UserModel) request.getAttribute("authUser");
    if (user != null) {
%>
<header class="container">
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <ul class="nav navbar-nav">
                <li><a href="${PATH}/<%= PageList.PAGE_RIDDLES %>">Загадки</a></li>
                <li><a href="${PATH}/<%= PageList.PAGE_PLAYERS %>">Отгадчики</a></li>
                <li><a href="${PATH}/<%= PageList.PAGE_RIDDLE_MINE %>">Мои загадки</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">${authUser.getName()} <span class="caret"></span></a>
                    <ul class="dropdown-menu">

                        <li><a href="${PATH}/${EXIT_URL}">Выход</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </nav>
</header>
<% } %>
<div class="container">
    <jsp:doBody/>
</div>
<footer>

</footer>
<script src="${PATH}/app/js/app.js"></script>
</body>
</html>