-- joker
-- krypton
INSERT INTO users(id, username, password) VALUES
(1, 'batman', '$2y$12$p8Zsas28dCEVJjfIzRjvgeM5O3oWqgSExWlq0xPsbD4aDYzyaiBIK'),
(2, 'superman', '$2y$12$6KfZMhZ.nrQqaXksMRh/ueATPYsXWPRhSzQjwMsbJ/JvX49y65vBO');

INSERT INTO user_roles(id, username, role_name) VALUES
(1, 'batman', 'USER'),
(2, 'superman', 'ADMIN'),
(3, 'superman', 'USER');
