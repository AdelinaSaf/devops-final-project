<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<nav>
    <ul>
        <div class="logo">
            <li><h1 class="site-title">Culinary Exchange</h1></li>
        </div>
        <li><a href="${pageContext.request.contextPath}/">Главная</a></li>
        <sec:authorize access="hasRole('ADMIN')">
            <a href="${pageContext.request.contextPath}/admin">Страинца администратора</a>
        </sec:authorize>
        <sec:authorize access="isAuthenticated()">
            <sec:authentication property="principal" var="userPrincipal"/>
            <li>
                <a href="${pageContext.request.contextPath}/profile/${userPrincipal.id}">
                    Профиль
                </a>
            </li>
            <li><a href="${pageContext.request.contextPath}/recipe/create">Создать рецепт</a></li>
            <li><a href="${pageContext.request.contextPath}/cookbook">Мои рецепты</a></li>
            <li><a href="${pageContext.request.contextPath}/favoriteRecipes">Любимые рецепты</a></li>
        </sec:authorize>

        <li><a href="${pageContext.request.contextPath}/search">Поиск рецептов</a></li>

        <form action="${pageContext.request.contextPath}/search" method="get">
            <input type="text" name="query" placeholder="Поиск рецептов...">
            <button type="submit">Поиск</button>
        </form>

        <div class="user-info">
            <sec:authorize access="isAuthenticated()">
                <p>Добро пожаловать, <sec:authentication property="name"/>!</p>
                <form action="${pageContext.request.contextPath}/logout" method="post">
                    <button type="submit">Выйти</button>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </sec:authorize>

            <sec:authorize access="!isAuthenticated()">
                <a href="${pageContext.request.contextPath}/login">Войти</a> |
                <a href="${pageContext.request.contextPath}/register">Зарегистрироваться</a>
            </sec:authorize>
        </div>
    </ul>

</nav>


<div class="messages">
    <c:if test="${not empty messages}">
        <c:forEach var="message" items="${messages}">
            <div class="message">${message}</div>
        </c:forEach>
    </c:if>
</div>


