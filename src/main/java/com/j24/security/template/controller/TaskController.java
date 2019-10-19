package com.j24.security.template.controller;

import com.j24.security.template.model.TodoTask;
import com.j24.security.template.service.AccountService;
import com.j24.security.template.service.TodoTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(path = "/task/")
public class TaskController {
    @Autowired
    private TodoTaskService todoTaskService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/add")
    public String taskForm(Model model, TodoTask task) {
        model.addAttribute("task", task);
        return "todotask-form";
    }

    @PostMapping("/add")
    public String taskSubmit(TodoTask task, Principal principal) {
        todoTaskService.add(task, principal.getName());

        return "redirect:/task/list/current";
    }

    @GetMapping("/list/current")
    public String listCurrent(Model model, Principal principal) {
        model.addAttribute("tasks", todoTaskService.getAllCurrent(principal.getName()));
        return "todotask-list";
    }

    @GetMapping("/list/archived")
    public String listArchived(Model model, Principal principal) {
        model.addAttribute("tasks", todoTaskService.getAllArchived(principal.getName()));
        return "todotask-list";
    }

    @GetMapping("/setDone")
    public String setDone(@RequestParam(name = "taskId") Long taskId,
                          Principal principal,
                          HttpServletRequest request) {
        todoTaskService.setDone(taskId, principal.getName());

        return "redirect:"+request.getHeader("referer");
    }

    @GetMapping("/setTodo")
    public String setTodo(@RequestParam(name = "taskId") Long taskId,
                          Principal principal,
                          HttpServletRequest request) {
        todoTaskService.setTodo(taskId, principal.getName());
        return "redirect:"+request.getHeader("referer");
    }

    @GetMapping("/setArchived")
    public String setArchived(@RequestParam(name = "taskId") Long taskId,
                              Principal principal,
                              HttpServletRequest request) {
        todoTaskService.setArchived(taskId, principal.getName());
        return "redirect:"+request.getHeader("referer");
    }

    @GetMapping("/user")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public String userChooser(Model model) {
        model.addAttribute("accounts", accountService.getAll());

        return "user-chooser";
    }

    @PostMapping("/user")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public String userTasks(Model model, String selectedUser) {
        List<TodoTask> all = todoTaskService.getAllArchived(selectedUser);
        all.addAll(todoTaskService.getAllCurrent(selectedUser));

        model.addAttribute("tasks", all);
        return "todotask-list";
    }
}
