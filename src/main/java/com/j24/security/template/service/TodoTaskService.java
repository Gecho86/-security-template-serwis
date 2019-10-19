package com.j24.security.template.service;

import com.j24.security.template.model.Account;
import com.j24.security.template.model.TodoTask;
import com.j24.security.template.model.TodoTaskStatus;
import com.j24.security.template.repository.AccountRepository;
import com.j24.security.template.repository.TodoTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoTaskService {
    @Autowired
    private TodoTaskRepository todoTaskRepository;
    @Autowired
    private AccountRepository accountRepository;

    public void add(TodoTask task, String name) {
        Optional<Account> accountOptional = accountRepository.findByUsername(name);
        if(accountOptional.isPresent()) {
            Account account = accountOptional.get();
            task.setUser(account);
            task.setTodoTaskStatus(TodoTaskStatus.TODO);

            todoTaskRepository.save(task);
        }
    }

    public List<TodoTask> getAll() {
        return todoTaskRepository.findAll();
    }

    public List<TodoTask> getAllCurrent(String name) {
        Optional<Account> accountOptional = accountRepository.findByUsername(name);
        if(accountOptional.isPresent()){
            Account account = accountOptional.get();

            return account.getTasks()
                    .stream()
                    .filter(task -> task.getTodoTaskStatus() != TodoTaskStatus.ARCHIVED)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<TodoTask> getAllArchived(String name) {
        Optional<Account> accountOptional = accountRepository.findByUsername(name);
        if(accountOptional.isPresent()){
            Account account = accountOptional.get();

            return account.getTasks()
                    .stream()
                    .filter(task -> task.getTodoTaskStatus() == TodoTaskStatus.ARCHIVED)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public void setDone(Long taskId, String name) {
        if(!checkIfTaskIsOwnedByUser(taskId, name)){
            return;
        }
        TodoTask task = todoTaskRepository.getOne(taskId);
        task.setTodoTaskStatus(TodoTaskStatus.DONE);
        task.setTimeFinished(LocalDateTime.now());
        todoTaskRepository.save(task);
    }

    private boolean checkIfTaskIsOwnedByUser(Long taskId, String name){
        Optional<Account> accountOptional = accountRepository.findByUsername(name);
        if(accountOptional.isPresent()){
            Account account = accountOptional.get();

            return account.getTasks().stream().anyMatch(task -> task.getId().equals(taskId));
        }
        return false;
    }

    public void setTodo(Long taskId, String name) {
        if(!checkIfTaskIsOwnedByUser(taskId, name)){
            return;
        }
        TodoTask task = todoTaskRepository.getOne(taskId);
        task.setTodoTaskStatus(TodoTaskStatus.TODO);
        task.setTimeFinished(null);
        todoTaskRepository.save(task);
    }

    public void setArchived(Long taskId, String name) {
        if(!checkIfTaskIsOwnedByUser(taskId, name)){
            return;
        }
        TodoTask task = todoTaskRepository.getOne(taskId);
        task.setTodoTaskStatus(TodoTaskStatus.ARCHIVED);
        todoTaskRepository.save(task);
    }
}
