<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Рецепт</title>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script>
        const recipeId = ${param.recipeId};
        let recipeData = {};

        function loadRecipe() {
            axios.get(`/api/recipes/${recipeId}`)
                .then(response => {
                    recipeData = response.data;
                    renderRecipe();
                })
                .catch(error => console.error(error));
        }

        function renderRecipe() {
            document.getElementById('recipe-name').innerText = recipeData.name;
            document.getElementById('recipe-description').innerText = recipeData.description;
        }

        function toggleFavorite() {
            const method = recipeData.isFavorite ? 'delete' : 'post';
            axios[method](`/api/recipes/${recipeId}/favorite`)
                .then(() => {
                    recipeData.isFavorite = !recipeData.isFavorite;
                    updateFavoriteButton();
                });
        }

        function updateFavoriteButton() {
            const btn = document.getElementById('favorite-btn');
            btn.innerText = recipeData.isFavorite
                ? 'Удалить из избранного'
                : 'Добавить в избранное';
            btn.className = recipeData.isFavorite
                ? 'btn btn-danger'
                : 'btn btn-primary';
        }

        // Загружаем рецепт при открытии страницы
        window.onload = loadRecipe;
    </script>
</head>
<body>
<h1 id="recipe-name"></h1>
<p id="recipe-description"></p>

<button id="favorite-btn" onclick="toggleFavorite()">
    Добавить в избранное
</button>

<div id="ingredients-container"></div>
<div id="steps-container"></div>

<div id="comments-section"></div>
</body>
</html>