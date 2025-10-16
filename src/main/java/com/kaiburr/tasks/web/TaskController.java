package com.kaiburr.tasks.web;

import com.kaiburr.tasks.Task;
import com.kaiburr.tasks.TaskExecution;
import com.kaiburr.tasks.repo.TaskRepo;
import com.kaiburr.tasks.service.CommandValidator;
import com.kaiburr.tasks.service.ShellRunner;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskRepo repo;
    private final CommandValidator validator;
    private final ShellRunner runner;

    public TaskController(TaskRepo repo, CommandValidator validator, ShellRunner runner) {
        this.repo = repo;
        this.validator = validator;
        this.runner = runner;
    }

    @GetMapping("/tasks")
    public ResponseEntity<?> getTasks(@RequestParam(name = "id", required = false) String id) {
        if (id == null) return ResponseEntity.ok(repo.findAll());
        return repo.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found"));
    }

    @PutMapping("/tasks")
    public ResponseEntity<?> putTask(@Valid @RequestBody Task t) {
        validator.validate(t.getCommand());
        if (t.getId() == null || t.getId().isBlank()) t.setId(null);
        return ResponseEntity.ok(repo.save(t));
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        if (!repo.existsById(id)) return ResponseEntity.status(404).body("Task not found");
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tasks/find")
    public ResponseEntity<?> find(@RequestParam String name) {
        List<Task> r = repo.findByNameContainingIgnoreCase(name);
        if (r.isEmpty()) return ResponseEntity.status(404).body("No tasks found");
        return ResponseEntity.ok(r);
    }

    @PutMapping("/tasks/{id}/execution")
    public ResponseEntity<?> exec(@PathVariable String id) throws Exception {
        Task t = repo.findById(id).orElse(null);
        if (t == null) return ResponseEntity.status(404).body("Task not found");
        validator.validate(t.getCommand());

        TaskExecution te = runner.run(t.getCommand());

        t.getTaskExecutions().add(te);
        repo.save(t);
        return ResponseEntity.ok(te);
    }
}
