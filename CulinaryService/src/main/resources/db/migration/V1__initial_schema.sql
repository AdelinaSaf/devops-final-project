CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       avatar VARCHAR(255) DEFAULT 'default-avatar.jpg',
                       created_at VARCHAR(255),
                       is_admin BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE preferences (
                             id BIGSERIAL PRIMARY KEY,
                             name VARCHAR(255) UNIQUE
);

CREATE TABLE recipes (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description TEXT,
                         category VARCHAR(255) NOT NULL,
                         preparation_time INTEGER,
                         servings INTEGER,
                         ingredients TEXT,
                         steps TEXT,
                         created_at VARCHAR(255) NOT NULL,
                         cover_image_path VARCHAR(255),
                         user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE comments (
                          id BIGSERIAL PRIMARY KEY,
                          content TEXT,
                          created_at VARCHAR(255) NOT NULL,
                          recipe_id BIGINT NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,
                          user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE recipe_images (
                               id BIGSERIAL PRIMARY KEY,
                               path VARCHAR(255) NOT NULL,
                               recipe_id BIGINT NOT NULL REFERENCES recipes(id) ON DELETE CASCADE
);

CREATE TABLE ratings (
                         id BIGSERIAL PRIMARY KEY,
                         rating DOUBLE PRECISION NOT NULL,
                         recipe_id BIGINT NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,
                         user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE user_favorite_recipes (
                                       id BIGSERIAL PRIMARY KEY,
                                       user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                       recipe_id BIGINT NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,
                                       UNIQUE (user_id, recipe_id)
);

CREATE TABLE user_preferences (
                                  id BIGSERIAL PRIMARY KEY,
                                  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                  preference_id BIGINT NOT NULL REFERENCES preferences(id) ON DELETE CASCADE,
                                  UNIQUE (user_id, preference_id)
);