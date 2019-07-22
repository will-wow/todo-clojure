CREATE TABLE todo
(
  id serial PRIMARY KEY,
  title text NOT NULL,
  done boolean DEFAULT False,
  created_at timestamp NOT NULL
);
