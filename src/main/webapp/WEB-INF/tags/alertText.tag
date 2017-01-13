<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="Simple Wrapper Tag" pageEncoding="UTF-8" %>
<%@attribute name="type" required="false" %>
<%@attribute name="text" required="false" %>

<c:if test="${not empty text}">
    <div class="alert alert-${type == null ? "danger" : type}" role="alert">
        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
            ${text}
    </div>
</c:if>