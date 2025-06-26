<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/header.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>




<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Редактировать рецепт</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/normalize.8.0.1.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/editRecipe.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css">
</head>
<body>

<div class="edit-recipe-container">
    <h1>Редактировать рецепт</h1>

    <form action="${pageContext.request.contextPath}/recipe/edit/${recipe.id}" method="post" enctype="multipart/form-data">
        <input type="hidden" name="id" value="${recipe.id}"/>

        <div class="form-group">
            <label for="name">Название рецепта:</label>
            <input type="text" id="name" name="name" value="${recipe.name}" required/>
        </div>

        <div class="form-group">
            <label>Обложка рецепта:</label>
            <c:if test="${not empty recipe.coverImagePath}">
                <img src="${pageContext.request.contextPath}/image?file=${recipe.coverImagePath}"
                     alt="Обложка рецепта" width="300">
            </c:if>
        </div>

        <div class="form-group">
            <label for="description">Описание:</label>
            <textarea id="description" name="description" required>${recipe.description}</textarea>
        </div>

        <div class="form-group">
            <label for="category">Категория рецепта:</label>
            <select id="category" name="category" class="form-select" required>
                <!-- Исправлено: categories -> preferences -->
                <option value="">Выберите категорию</option>
                <c:forEach items="${preferences}" var="cat">
                    <option value="${cat}"
                        ${recipe.category == cat ? 'selected' : ''}>
                            ${cat}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="preparationTime">Время приготовления (мин):</label>
            <input type="number" id="preparationTime" name="preparationTime" value="${recipe.preparationTime}"
                   required/>
        </div>

        <div class="form-group">
            <label for="servings">Порции:</label>
            <input type="number" id="servings" name="servings" value="${recipe.servings}" required/>
        </div>

        <div class="form-group">
            <label for="ingridients">Ингредиенты:</label>
            <textarea id="ingridients" name="ingridients" required>${recipe.ingredients}</textarea>
        </div>

        <div class="form-group">
            <label for="steps">Шаги приготовления (через запятую):</label>
            <textarea id="steps" name="steps" required>${recipe.steps}</textarea>
        </div>


        <div class="form-group">
            <label>Дата создания:</label>
            <p>${recipe.formattedCreatedAt}</p>
        </div>

        <div class="form-group">
            <label>Дополнительные изображения:</label>
            <c:if test="${not empty recipe.additionalImages}">
                <div id="recipeCarousel" class="carousel slide" data-bs-ride="carousel">
                    <!-- Индикаторы -->
                    <div class="carousel-indicators">
                        <c:forEach var="i" begin="0" end="${fn:length(recipe.additionalImages) - 1}">
                            <button type="button" data-bs-target="#recipeCarousel" data-bs-slide-to="${i}"
                                    class="${i == 0 ? 'active' : ''}"
                                    aria-current="${i == 0 ? 'true' : 'false'}"
                                    aria-label="Slide ${i + 1}"></button>
                        </c:forEach>
                    </div>

                    <div class="carousel-inner">
                        <c:forEach var="imagePath" items="${recipe.additionalImages}" varStatus="status">
                            <div class="carousel-item ${status.first ? 'active' : ''}">
                                <img src="${pageContext.request.contextPath}/image?file=${imagePath}"
                                     alt="Изображение рецепта" class="d-block w-100">
                            </div>
                        </c:forEach>
                    </div>

                    <button class="carousel-control-prev" type="button" data-bs-target="#recipeCarousel"
                            data-bs-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Предыдущий</span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#recipeCarousel"
                            data-bs-slide="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Следующий</span>
                    </button>
                </div>
            </c:if>

        </div>
        <label for="coverImage">Выбрать новую обложку рецепта:</label>
        <input type="file" id="coverImage" name="coverImage" multiple accept="image/*">
        <label for="images">Выбрать новые изображения(можно выбрать несколько):</label>
        <input type="file" id="images" name="images" multiple accept="image/*">


        <button type="submit" class="btn btn-primary">Сохранить изменения</button>
        <a href="${pageContext.request.contextPath}/recipe/${recipe.id}"
           class="btn btn-secondary">Отмена</a>

    </form>
</div>

<%@ include file="/WEB-INF/views/footer.jsp" %>
</body>
</html>

