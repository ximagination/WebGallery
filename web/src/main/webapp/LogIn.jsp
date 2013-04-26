<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="static visible.LogIn.AUTENTIFICATION" %>
<%@ page import="static visible.LogIn.PASSWORD" %>
<%@ page import="static visible.LogIn.LOGIN" %>
<%@ page import="static visible.LogIn.*" %>
<%@ page import="static invisible.ImageUpload.*" %>

<%@ page import="invisible.ImageUpload" %>
<%@ page import="visible.LogIn" %>
<%@ page import="visible.LogOut" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>WebGallery</title>
    <link type="text/css"
          href="/css/bootstrap.css"
          media="all" rel="stylesheet">
</head>
<body>

<c:if test="${sessionScope.warning != null}">
    <span
            style="width: 400px"
            class="label label-warning">
            Authentication failed because
            <c:out
                    value="${sessionScope.warning}">
            </c:out>
    </span>
</c:if>

<div class="navbar navbar-inverse">
    <div class="navbar-inner">
        <div class="container">

            <a class="brand" href="#">WebGallery</a>

            <div class="nav-collapse collapse">

                <form class="navbar-form pull-right" action="<%=LogIn.class.getSimpleName()%>" method="post"
                      accept-charset="UTF-8">

                    <input name="<%=LOGIN%>"
                           class="input"
                           type="text"
                           placeholder="Login"
                           maxlength="32">

                    <input name="<%=PASSWORD%>"
                           type="password"
                           placeholder="Password"
                           maxlength="32">

                    <strong>
                        <button
                                class="btn"
                                type="submit"
                                name="<%=AUTENTIFICATION%>">Autentificate
                        </button>
                    </strong>
                    <strong>
                        <button
                                class="btn"
                                type="submit"
                                name="<%=REGISTRATION%>">Register
                        </button>
                    </strong>
                </form>
            </div>
        </div>
    </div>
</div>

<c:if test="${sessionScope.user != null}">
    <form action="<%=LogOut.class.getSimpleName()%>" method="post" accept-charset="UTF-8">
        <span style="width: 200px" class="label label-success">Success authenticated <c:out
                value="${sessionScope.user.login}"/> </span>
        <button type="submit" class="btn">Log out</button>
    </form>
</c:if>


<c:if test="${sessionScope.upload_message != null}">
    <span
            class="label label-warning">
            <c:out
                    value="${sessionScope.upload_message}">
            </c:out>
    </span>
</c:if>

<c:if test="${sessionScope.user != null}">
    <form
            class="form-horizontal"
            action="<%=ImageUpload.class.getSimpleName()%>"
            method="post"
            enctype="multipart/form-data"
            accept-charset="UTF-8">

        <div class="control-group">
            <label class="control-label" for="<%=NAME%>">Name</label>

            <div class="controls">
                <input
                        name="<%=NAME%>"
                        type="text"
                        id="<%=NAME%>"
                        placeholder="Name">
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="<%=COMMENT%>">Comment</label>

            <div class="controls">
                <input
                        name="<%=COMMENT%>"
                        type="text"
                        id="<%=COMMENT%>"
                        placeholder="Comment">
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="<%=PATH%>">Path to file</label>

            <div class="controls">
                <input
                        name="<%=PATH%>"
                        type="file"
                        accept="image/png, image/jpeg"
                        id="<%=PATH%>"
                        placeholder="Path to file">
            </div>
        </div>

        <div class="control-group">
            <div class="controls">
                <button
                        class="btn"
                        type="submit"
                        name="<%=UPLOAD%>">Send
                </button>
            </div>
        </div>

    </form>
</c:if>

</body>
</html>