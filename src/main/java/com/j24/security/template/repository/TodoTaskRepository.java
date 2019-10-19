package com.j24.security.template.repository;

import com.j24.security.template.model.TodoTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoTaskRepository extends JpaRepository<TodoTask, Long> {

}
