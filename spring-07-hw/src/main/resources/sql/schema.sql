DROP TABLE IF EXISTS `books`;
DROP TABLE IF EXISTS `authors`;
DROP TABLE IF EXISTS `genres`;

CREATE TABLE `authors` (
   `id` BIGINT PRIMARY KEY,
   `name` VARCHAR(255) NOT NULL
);

CREATE TABLE `genres` (
    `id` BIGINT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL
);

CREATE TABLE `books` (
    `id` BIGINT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `author_id` BIGINT,
    `genre_id` BIGINT,
    CONSTRAINT `fk_books_authors` FOREIGN KEY (`author_id`) REFERENCES `authors`(`id`),
    CONSTRAINT `fk_books_genres` FOREIGN KEY (`genre_id`) REFERENCES `genres`(`id`)
);
