<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Вход в систему</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/normalize.8.0.1.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/login.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css">
    <style>
        .error-message {
            color: #d32f2f;
            font-size: 14px;
            margin-top: 5px;
            padding: 5px 10px;
            background-color: #ffebee;
            border-radius: 4px;
            border: 1px solid #ef9a9a;
        }
        .error-field {
            border: 1px solid #d32f2f !important;
        }
    </style>
</head>
<body>
<header>
    <nav>
        <a href="${pageContext.request.contextPath}/main">Главная</a>
    </nav>
</header>

<main>
    <h2>Вход в систему</h2>

    <c:if test="${param.error != null}">
        <div class="error-message">
            Неверный email или пароль. Пожалуйста, попробуйте снова.
        </div>
    </c:if>

    <form:form
            action="${pageContext.request.contextPath}/login"
            method="post"
            modelAttribute="loginForm">

        <div>
            <label for="email">Email:</label>
            <form:input
                    path="email"
                    id="email"
                    type="email"
                    required="true"
                    cssClass="${not empty emailError ? 'error-field' : ''}" />
            <form:errors path="email" cssClass="error-message" element="div" />
        </div>

        <div>
            <label for="password">Пароль:</label>
            <form:input
                    path="password"
                    id="password"
                    type="password"
                    required="true"
                    cssClass="${not empty passwordError ? 'error-field' : ''}" />
            <form:errors path="password" cssClass="error-message" element="div" />
        </div>

        <c:if test="${not empty param.returnUrl}">
            <input type="hidden" name="returnUrl" value="${param.returnUrl}">
        </c:if>

        <div>
            <button type="submit">Войти</button>
        </div>
    </form:form>

    <div class="register-link">
        <a href="${pageContext.request.contextPath}/register">Ещё нет аккаунта? Зарегистрироваться</a>
    </div>
</main>
</body>
</html>
