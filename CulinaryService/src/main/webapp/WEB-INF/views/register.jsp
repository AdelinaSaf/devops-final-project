<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Регистрация</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/normalize.8.0.1.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/register.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css">
</head>
<body>
<header>
    <nav>
        <a href="${pageContext.request.contextPath}/main">Главная</a>
    </nav>
</header>

<main>
    <h2>Регистрация нового пользователя</h2>

    <form:form method="post" action="${pageContext.request.contextPath}/register" modelAttribute="user">
        <div>
            <label for="name">Имя:</label>
            <form:input path="username" id="name" required="true" />
            <form:errors path="username" cssClass="error" />
        </div>

        <div>
            <label for="reg-email">Email:</label>
            <form:input path="email" id="reg-email" type="email" required="true" />
            <form:errors path="email" cssClass="error" />
        </div>

        <div>
            <label for="password">Пароль:</label>
            <form:password path="password" id="password" name="password" required="true" />
            <form:errors path="password" cssClass="error" />
        </div>

        <div>
            <label for="reg-password-confirm">Подтвердите пароль:</label>
            <form:password path="passwordConfirm" id="reg-password-confirm" required="true" />
            <form:errors path="passwordConfirm" cssClass="error" />
        </div>

        <div>
            <label>Выберите предпочтения в кухне:</label>
            <div>
                <c:forEach var="preference" items="${preferences}">
                    <div>
                        <!-- Используем id предпочтения как значение -->
                        <form:checkbox path="preferenceIds"
                                       value="${preference.id}"
                                       id="preference-${preference.id}" />
                        <label for="preference-${preference.id}">${preference.preferenceName}</label>
                    </div>
                </c:forEach>
            </div>
            <form:errors path="preferenceIds" cssClass="error" />
        </div>

        <div>
            <button type="submit">Зарегистрироваться</button>
        </div>
    </form:form>

    <p>Уже есть аккаунт? <a href="${pageContext.request.contextPath}/login">Войти</a></p>
</main>

<%@ include file="/WEB-INF/views/footer.jsp" %>
</body>
</html>
