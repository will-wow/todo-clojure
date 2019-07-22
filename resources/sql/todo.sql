-- :name all-todos :? :*
-- :doc Get all todos
select *
from todo
order by done, created_at;

-- :name create-todo :<! :1
-- :doc Create a new todo
insert into todo
  (title, created_at)
values
  (:title, :created_at)
returning id;

-- :name update-todo :<! :1
-- :doc Update a Todo by ID
update todo
set 
  title = :title,
  done = :done
where id = :id
returning id;

-- :name get-todo :? :1
-- :doc Get a todo by ID
select *
from todo
where id = :id;

-- :name delete-todo :! :n
-- :doc Delete a todo by id
DELETE FROM todo
WHERE id = :id;