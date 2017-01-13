<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="Simple Wrapper Tag" pageEncoding="UTF-8" %>

<c:if test="${not empty exception_class && not empty exception_message && not empty exception_trace}">
    <div class="alert alert-danger" role="alert">
        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
        <b>${exception_class}</b>: ${exception_message}
        <br/>
        <small><code>${exception_trace}</code></small>
    </div>
</c:if>