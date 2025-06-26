<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Поиск рецептов</title>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/normalize.8.0.1.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/search.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/mainPage.css">
</head>
<body>

<div class="search-container">
    <h1>Поиск рецептов</h1>
    <div class="form">
        <input type="text" id="search-query" placeholder="Введите ключевые слова..."
               value="${param.query != null ? param.query : ''}">

        <label for="search-category">Категория:</label>
        <select id="search-category">
            <option value="">Все категории</option>
            <c:forEach var="preference" items="${preferences}">
                <option value="${preference}">${preference}</option>
            </c:forEach>
        </select>

        <button onclick="searchRecipes()">Поиск</button>
    </div>
</div>

<div class="results-container" id="results-container">

</div>

<%@ include file="/WEB-INF/views/footer.jsp" %>
<script>
    const contextPath = '${pageContext.request.contextPath}';
    function searchRecipes() {
        const query = document.getElementById('search-query').value;
        const category = document.getElementById('search-category').value;

        axios.get('${pageContext.request.contextPath}/api/recipes/search', {
            params: {
                query: query,
                category: category
            }
        })
            .then(response => {
                const container = document.getElementById('results-container');
                container.innerHTML = '<h2>Результаты поиска:</h2>';

                if (response.data.length === 0) {
                    container.innerHTML += '<p>К сожалению, рецепты не найдены. Попробуйте изменить параметры поиска.</p>';
                    return;
                }

                const recipeList = document.createElement('ul');
                recipeList.className = 'recipe-list';

                response.data.forEach(recipe => {
                    const listItem = document.createElement('li');
                    listItem.className = 'recipe-item';

                    const link = document.createElement('a');
                    link.href = `${pageContext.request.contextPath}/recipe/${recipe.id}`;

                    const coverWrapper = document.createElement('div');
                    coverWrapper.className = 'recipe-cover-wrapper';

                    if (recipe.coverImagePath) {
                        const image = document.createElement('img');
                        image.src = contextPath + `/image?file=`+ encodeURIComponent(recipe.coverImagePath);
                        image.alt = recipe.name;
                        image.className = 'recipe-cover';
                        coverWrapper.appendChild(image);
                    }

                    const title = document.createElement('h3');
                    title.textContent = recipe.name;

                    const description = document.createElement('p');
                    description.textContent = recipe.description;

                    link.appendChild(coverWrapper);
                    link.appendChild(title);
                    link.appendChild(description);

                    listItem.appendChild(link);
                    recipeList.appendChild(listItem);
                });

                container.appendChild(recipeList);
            })
            .catch(error => {
                console.error('Ошибка при поиске рецептов:', error);
                document.getElementById('results-container').innerHTML = `
                <p>Произошла ошибка при выполнении поиска. Пожалуйста, попробуйте позже.</p>
            `;
            });
    }

    document.addEventListener('DOMContentLoaded', () => {
        const urlParams = new URLSearchParams(window.location.search);
        const queryParam = urlParams.get('query');
        const categoryParam = urlParams.get('category');

        if (queryParam) {
            document.getElementById('search-query').value = queryParam;
        }
        if (categoryParam) {
            document.getElementById('search-category').value = categoryParam;
        }

        searchRecipes();
    });

</script>
</body>
</html>

