DELETE FROM tasks WHERE id = 1;

ALTER TABLE tasks ALTER COLUMN id RESTART WITH 1;