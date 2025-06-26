<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/header.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Профиль пользователя</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/normalize.8.0.1.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/profile.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css">
</head>
<body>

<div class="profile-container">
    <h1>Профиль пользователя</h1>

    <p class="user-info">
    <div class="avatar-container">
        <img src="${pageContext.request.contextPath}/image?file=${profileUser.avatarPath}" alt="Аватар пользователя"
             class="avatar">

    </div>

    <h2>${profileUser.username}</h2>
    <p>Email: ${profileUser.email}</p>
    <p>Любимые кухни:
    <ul class="user-preference-list">
        <c:forEach var="preference" items="${profileUser.preferences}">
            <li>${preference.name}</li>
        </c:forEach>
    </ul>
    <p>Рейтинг: ${profileUser.userRating}</p>
    <p>Профиль был создан: ${profileUser.createdAt}</p>

    <c:if test="${not empty currentUserId && currentUserId == profileUser.id}">
        <a href="${pageContext.request.contextPath}/profile/edit" class="btn">Редактировать профиль</a>
    </c:if>
    <c:if test="${not empty currentUserId && currentUserId == profileUser.id}">
        <form action="${pageContext.request.contextPath}/profile/${profileUser.id}/delete" method="post">
            <input type="hidden" name="action" value="delete">
            <button type="submit" class="delete-btn">Удалить профиль</button>
        </form>
    </c:if>
</div>

<div class="user-recipes">
    <h3>Мои рецепты</h3>
    <p>Создано ${profileUser.createdRecipesCount} рецептов.</p>
    <c:if test="${not empty currentUserId && currentUserId == profileUser.id}">
        <a href="${pageContext.request.contextPath}/cookbook" class="btn">Посмотреть мои рецепты</a>
    </c:if>
</div>

<div class="saved-recipes">
    <h3>Избранные рецепты</h3>
    <p>Добавлено ${profileUser.favoriteRecipesCount} рецептов в избранное.</p>
    <c:if test="${not empty currentUserId && currentUserId == profileUser.id}">
        <a href="${pageContext.request.contextPath}/favoriteRecipes" class="btn">Перейти к избранным</a>
    </c:if>
</div>

<div class="interaction-history">
    <h3>История взаимодействий</h3>
    <c:if test="${not empty profileUser.comments}">
        <ul>
            <c:forEach var="comment" items="${profileUser.comments}">
                <li>
                    <a href="${pageContext.request.contextPath}/recipe/${comment.recipeId}#comment-${comment.id}">
                            ${comment.content}
                    </a> - ${comment.createdAt}
                </li>
            </c:forEach>
        </ul>
    </c:if>
    <c:if test="${empty profileUser.comments}">
        <p>Нет истории взаимодействий.</p>
    </c:if>
</div>


</div>

<%@ include file="/WEB-INF/views/footer.jsp" %>

</body>
</html>

