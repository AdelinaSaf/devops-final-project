<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/header.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${recipe.name}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/normalize.8.0.1.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/showRecipe.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css">
</head>
<body>
<div class="recipe-container">
    <h1>${recipe.name}</h1>
    <p>автор:
        <a href="${pageContext.request.contextPath}/profile/${recipe.userId}">
            ${recipe.authorName}
        </a></p>


    <c:if test="${not empty recipe.coverImagePath}">
        <img src="${pageContext.request.contextPath}/image?file=${recipe.coverImagePath}" alt="Обложка рецепта"
             class="recipe-cover-image">
    </c:if>

    <div class="recipe-details">
        <p><strong>Описание:</strong> ${recipe.description}</p>
        <p><strong>Категории:</strong> ${recipe.category}</p>
        <p><strong>Время приготовления:</strong> ${recipe.preparationTime} минут</p>
        <p><strong>Порции:</strong> ${recipe.servings}</p>


        <h2>Ингредиенты (через запятую):</h2>
        <ul>
            <c:forEach var="ingredient" items="${recipe.ingredients}">
                <li>${ingredient}</li>
            </c:forEach>
        </ul>


        <h2>Шаги приготовления (через запятую):</h2>
        <ol>
            <c:forEach var="step" items="${recipe.steps}">
                <li>${step}</li>
            </c:forEach>
        </ol>
        <c:if test="${not empty additionalImages}">
            <div id="recipeCarousel" class="carousel slide" data-bs-ride="carousel">
                <!-- Индикаторы -->
                <div class="carousel-indicators">
                    <c:forEach var="i" begin="0" end="${fn:length(additionalImages) - 1}">
                        <button type="button" data-bs-target="#recipeCarousel" data-bs-slide-to="${i}"
                                class="${i == 0 ? 'active' : ''}"
                                aria-current="${i == 0 ? 'true' : 'false'}"
                                aria-label="Slide ${i + 1}"></button>
                    </c:forEach>
                </div>

                <!-- Слайды -->
                <div class="carousel-inner">
                    <c:forEach var="imagePath" items="${additionalImages}" varStatus="status">
                        <div class="carousel-item ${status.first ? 'active' : ''}">
                            <img src="${pageContext.request.contextPath}/image?file=${imagePath}"
                                 alt="Изображение рецепта" class="d-block w-100">
                        </div>
                    </c:forEach>
                </div>

                <!-- Кнопки управления -->
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

        <p><strong>Дата создания:</strong> ${recipe.formattedCreatedAt}</p>
    </div>

    <div class="actions">
        <form action="${pageContext.request.contextPath}/recipe/${recipe.id}/pdf" method="post" class="pdf-btn">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <button type="submit" class="btn-pdf">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
                    <path d="M.5 9.9a.5.5 0 0 1 .5.5v2.5a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-2.5a.5.5 0 0 1 1 0v2.5a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2v-2.5a.5.5 0 0 1 .5-.5z"/>
                    <path d="M7.646 11.854a.5.5 0 0 0 .708 0l3-3a.5.5 0 0 0-.708-.708L8.5 10.293V1.5a.5.5 0 0 0-1 0v8.793L5.354 8.146a.5.5 0 1 0-.708.708l3 3z"/>
                </svg>
                Скачать PDF
            </button>
        </form>
        <c:if test="${user != null}">
            <button class="favorite-btn ${isFavorite ? 'active' : ''}"
                    data-recipe-id="${recipe.id}"
                    id="favorite-button">
                    ${isFavorite ? 'Удалить из избранного' : 'Сохранить в избранное'}
            </button>
        </c:if>


        <c:if test="${recipe.userId == user.id}">
            <form action="${pageContext.request.contextPath}/recipe/edit/${recipe.id}" class="edit-btn" method="get">
                <button type="submit">Редактировать</button>
            </form>
            <form action="${pageContext.request.contextPath}/recipe/${recipe.id}/delete" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <button type="submit"
                        onclick="return confirm('Вы уверены, что хотите удалить этот рецепт?');">Удалить
                </button>
            </form>
        </c:if>
    </div>

    <div class="rating">
        <h2>Средняя оценка: ${recipe.averageRating}</h2>
        <c:if test="${recipe.userId != user.id}">
            <form action="${pageContext.request.contextPath}/recipe/${recipe.id}/rate" method="post">
                <input type="hidden" name="action" value="rate">
                <input type="hidden" name="recipeId" value="${recipe.id}">
                <select name="rating" required>
                    <option value="">Выберите оценку</option>
                    <c:forEach var="i" begin="1" end="5">
                        <option value="${i}">
                            <c:choose>
                                <c:when test="${i == 1}">
                                    ${i} звезда
                                </c:when>
                                <c:when test="${i >= 2 && i <= 4}">
                                    ${i} звезды
                                </c:when>
                                <c:otherwise>
                                    ${i} звёзд
                                </c:otherwise>
                            </c:choose>
                        </option>
                    </c:forEach>
                </select>
                <button type="submit">Оценить</button>
            </form>
        </c:if>
    </div>

    <div class="comments-section">

        <h3>Добавить комментарий:</h3>
        <form action="${pageContext.request.contextPath}/recipe/${recipe.id}/comment" method="post">
            <input type="hidden" name="action" value="addComment">
            <textarea name="commentText" rows="4" required></textarea>
            <button type="submit">Отправить</button>
        </form>
        <h2>Комментарии:</h2>
        <c:choose>
            <c:when test="${not empty comments}">
                <c:forEach var="comment" items="${comments}">
                    <div class="comment" id="comment-${comment.id}">
                        <p><strong>${comment.username}:</strong> ${comment.content}</p>
                        <p><em>${comment.createdAt}</em></p>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p>Комментариев пока нет</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<%@ include file="/WEB-INF/views/footer.jsp" %>

<script>
    $(document).ready(function() {
        $('#favorite-button').click(function() {
            const recipeId = $(this).data('recipe-id');
            const isFavorite = $(this).hasClass('active');
            const method = isFavorite ? 'DELETE' : 'POST';
            const url = '${pageContext.request.contextPath}/api/recipes/' + recipeId + '/favorite';
            const button = $(this);

            $.ajax({
                url: url,
                method: method,
                success: function() {
                    button.toggleClass('active');
                    button.text(isFavorite ? 'Сохранить в избранное' : 'Удалить из избранного');
                },
                error: function(xhr) {
                    console.error('Ошибка:', xhr.responseText);
                    alert('Произошла ошибка. Пожалуйста, попробуйте снова.');
                }
            });
        });

        $('#search-input').on('input', function() {
            const query = $(this).val();

            $.get('${pageContext.request.contextPath}/api/recipes/search', { query: query }, function(recipes) {
                $('#recipes-container').empty();
                recipes.forEach(function(recipe) {
                    $('#recipes-container').append(`
                    <div class="recipe-item">
                        <a href="/recipe/${recipe.id}">
                            <img src="/image?file=${recipe.coverImagePath}" alt="${recipe.name}">
                            <h3>${recipe.name}</h3>
                        </a>
                    </div>
                `);
                });
            });
        });
    });
</script>
</body>
</html>

