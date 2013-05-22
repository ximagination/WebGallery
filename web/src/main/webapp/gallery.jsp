<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>WebGallery</title>
    <link type="text/css"
          href="/resources/css/bootstrap.css"
          media="all" rel="stylesheet">
</head>
<body>

<c:set var="canShow" value="${sessionScope.user != null and warning == null}"/>

<div class="navbar navbar-inverse">
    <div class="navbar-inner">
        <div class="container">

            <a class="brand" href="#">WebGallery</a>

            <c:if test="${canShow}">
                <ul class="nav nav-pills">
                    <li><a href="/Login">Upload</a></li>
                    <li class="active">
                        <a href="/Gallery">Gallery</a>
                    </li>
                </ul>
            </c:if>

            <div class="nav-collapse collapse">

                <f:form
                        commandName="login"
                        class="navbar-form pull-right"
                        action="Login"
                        method="post"
                        accept-charset="UTF-8">

                    <f:errors cssClass="label label-warning" path="login"/>
                    <f:errors cssClass="label label-warning" path="password"/>

                    <c:if test="${sessionScope.warning != null}">
                    <span
                            class="label label-warning">
                            Authentication failed because
                            <c:out
                                    value="${sessionScope.warning}">
                            </c:out>
                    </span>
                    </c:if>

                    <c:if test="${!canShow}">
                        <f:input
                                path="login"
                                class="input"
                                type="text"
                                placeholder="Login"
                                maxlength="32"/>

                        <f:input
                                path="password"
                                type="password"
                                placeholder="Password"
                                maxlength="32"/>

                        <button
                                class="btn"
                                type="submit"
                                name="autentification">Autentificate
                        </button>

                        <button
                                class="btn"
                                type="submit"
                                name="registration">Register
                        </button>
                    </c:if>
                </f:form>

                <c:if test="${canShow}">
                    <form
                            class="navbar-form pull-right"
                            action="Logout"
                            method="post"
                            accept-charset="UTF-8">
                        <button type="submit" class="btn">Log out</button>
                    </form>
                </c:if>

            </div>
        </div>
    </div>
</div>

<c:forEach var="array" items="${images}">
    <div class="row-fluid">
        <c:forEach var="o" items="${array}">
            <c:if test="${!empty o}">
                <div class="span4">
                    <h2>${o.name}</h2>

                    <div class="row" style="margin:10px">
                        <div class="span16">
                            <ul class="media-grid">
                                <a href="/Gallery/${o.id}?type=ORIGINAL">
                                    <img src="/Gallery/${o.id}?type=PREVIEW" alt=""/>
                                </a>
                            </ul>
                        </div>
                    </div>

                    <p><strong>Comment :</strong> ${o.comment}</p>

                    <p><strong>Pub Date :</strong> ${o.timestamp}</p>
                </div>
            </c:if>
        </c:forEach>
    </div>
</c:forEach>

<c:if test="${countOfPages > 1}">
    <c:set var="numberOfPaginatorPage" value="1"/>

    <div class="pagination pagination-centered">
        <ul>
            <li class="disabled"><a href="#">&laquo;</a></li>
            <c:forEach begin="1" end="${countOfPages}">
                <li class="active"><a href="/Gallery?pageId=<c:out
                        value="${numberOfPaginatorPage-1}"></c:out>">${numberOfPaginatorPage}</a></li>
                <c:set var="numberOfPaginatorPage" value="${numberOfPaginatorPage+1}"/>
            </c:forEach>
            <li class="disabled"><a href="#">&raquo;</a></li>
        </ul>
    </div>
</c:if>

</body>
</html>