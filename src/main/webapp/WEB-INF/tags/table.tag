<%@ tag import="ru.rap.common.PageList" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="Simple Wrapper Tag" pageEncoding="UTF-8" %>
<%@attribute name="list" type="java.util.List" required="true" %>
<%@attribute name="pageIndex" type="java.lang.Integer" required="false" %>
<%@attribute name="pageCount" type="java.lang.Integer" required="false" %>

${list} ${pageIndex} ${pageCount}

