INSERT INTO `authors` (`id`, `name`) VALUES (1, 'testAuthor1');
INSERT INTO `authors` (`id`, `name`) VALUES (2, 'testAuthor2');
INSERT INTO `authors` (`id`, `name`) VALUES (3, 'testAuthor3');
INSERT INTO `genres` (`id`, `name`) VALUES (1, 'testGenre1');
INSERT INTO `genres` (`id`, `name`) VALUES (2, 'testGenre2');
INSERT INTO `books` (`id`, `name`, `author_id`,`genre_id`) VALUES (1, 'testBook1', 2, 1);
INSERT INTO `books` (`id`, `name`, `author_id`,`genre_id`) VALUES (2, 'testBook2', 2, 2);
INSERT INTO `books` (`id`, `name`, `author_id`,`genre_id`) VALUES (3, 'testBook3', 2, 2);
INSERT INTO `books` (`id`, `name`, `author_id`,`genre_id`) VALUES (4, 'testBook4', 3, 1);
INSERT INTO `books` (`id`, `name`, `author_id`,`genre_id`) VALUES (5, 'testBook5', 3, 1);
INSERT INTO `books` (`id`, `name`, `author_id`,`genre_id`) VALUES (6, 'testBook6', 3, 2);
