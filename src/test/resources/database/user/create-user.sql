DELETE FROM users;
DELETE FROM roles;

INSERT INTO users(id, username, password, email, first_name, last_name)
VALUES (1, 'user', '$2a$12$mvTU2b7lsk4HT8JwSV2ZpOzZybrRd0zw0GmZCv47Id2RIiTkb8E5y', 'user@gmail.com', 'user', 'user');

INSERT INTO roles (id, name) VALUES (1, 'ROLE_USER'), (2, 'ROLE_ADMIN');
INSERT INTO users_roles (user_id, role_id) VALUES (1, 1);