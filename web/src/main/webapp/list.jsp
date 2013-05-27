<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:forEach var="o" items="${comments}">
    <div class="hero-unit">
        <h3><i>${o.userLogin}:</i></h3>

        <p>${o.comment}</p>

        <p>${o.timestamp}</p>
    </div>
</c:forEach>