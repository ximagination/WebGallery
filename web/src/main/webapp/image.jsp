<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
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
    <style type="text/css">

        .size {
            width: 95%;
            height: 95%;
            max-width: 1200px;
            max-height: 900px;
        }

        .scroll {
            height: 900px;
            width: 600px;
            border: 1px solid #ccc;
            font: 16px/26px Georgia, Garamond, Serif;
            overflow: auto;
        }
    </style>

    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>
    <script type="text/javascript">

        var date = new Date();

        function init() {
            fetch("");
        }

        function fetch(value) {
            $.ajax({
                type: "GET",
                content: "application/json",
                media: "application/json",
                accept: "application/json",
                url: "/Comment/all/${id}?date=" + value,
                success: function (msg) {
                    document.getElementById('scroll').innerHTML = msg;
                    date = new Date();
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert("Status: " + textStatus + " XMLHttpRequest " + XMLHttpRequest + " errorThrown " + errorThrown);
                }
            });
        }

        function submit(frm) {
            var text = frm.comment.text;

            $.ajax({
                type: "POST",
                content: "application/json",
                media: "application/json",
                accept: "application/json",
                url: "/Comment",
                data: text,
                success: function (msg) {
                    fetch(date);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert("Status: " + textStatus + " XMLHttpRequest " + XMLHttpRequest + " errorThrown " + errorThrown);
                }
            });
        }

    </script>
</head>
<body onload="init();">

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

<div class="container-fluid">
    <div class="row-fluid">
        <div class="span7">
            <div class="size">
                <img class="img-polaroid" src="/Gallery/${id}?type=ORIGINAL" alt=""/>

                <form class="navbar-form pull-left"
                      method="post"
                      action="/Comment"
                      accept-charset="UTF-8">
                    <input id="mComment" style="width: 840px" name="comment" type="text" class="span2">
                    <button onclick="submit(this.form);" class="btn">Add comment</button>
                </form>
                <button onclick="fetch(date);" class="btn">CHECK DATE</button>
            </div>
        </div>
        <div class="span5">
            <div id="scroll" class="scroll">
                <%--body inserted by js--%>
            </div>
        </div>
    </div>
</div>

</body>
</html>