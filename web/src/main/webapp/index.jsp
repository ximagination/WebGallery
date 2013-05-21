<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>

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
<c:set var="isUpload" value="${warning == null}"/>

<div class="navbar navbar-inverse">
    <div class="navbar-inner">
        <div class="container">

            <a class="brand" href="#">WebGallery</a>

            <c:if test="${canShow}">
                <ul class="nav nav-pills">
                    <li class="active">
                        <a href="/Login">Upload</a>
                    </li>
                    <li><a href="/Gallery">Gallery</a></li>
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

<c:if test="${sessionScope.upload_message != null}">
    <span
            class="label label-warning">
            <c:out
                    value="${sessionScope.upload_message}">
            </c:out>
    </span>
</c:if>

<c:if test="${canShow}">
    <form
            class="form-horizontal"
            action="ImageUpload"
            method="post"
            enctype="multipart/form-data"
            accept-charset="UTF-8">

        <div class="control-group">
            <label class="control-label" for="name">Name</label>

            <div class="controls">
                <input
                        name="name"
                        type="text"
                        id="name"
                        placeholder="Name">
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="comment">Comment</label>

            <div class="controls">
                <input
                        name="comment"
                        type="text"
                        id="comment"
                        placeholder="Comment">
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="path">Path to file</label>

            <div class="controls">
                <input
                        name="path"
                        type="file"
                        accept="image/png, image/jpeg"
                        id="path"
                        placeholder="Path to file">
            </div>
        </div>

        <div class="control-group">
            <div class="controls">
                <button
                        class="btn"
                        type="submit"
                        name="upload">Send
                </button>
            </div>
        </div>

    </form>
</c:if>

</body>
</html>