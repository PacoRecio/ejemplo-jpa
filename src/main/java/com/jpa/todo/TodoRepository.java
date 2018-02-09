package com.jpa.todo;

import org.springframework.data.repository.Repository;

/*
we create the repository that provides CRUD operations for Todo objects, we have to provide the following type parameters:

The type of the entity is Todo.
The type of the entity’s id field is Long
*/

interface TodoRepository extends Repository<Todo, Long> {

}
