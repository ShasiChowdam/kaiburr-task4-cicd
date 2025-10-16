package com.kaiburr.tasks.repo;

import com.kaiburr.tasks.model.Task;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepo extends MongoRepository<Task, String> {
    List<Task> findByNameContainingIgnoreCase(String name);
}
