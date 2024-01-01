package io.github.kitsf0x.springtodo.task;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class TaskDatabaseSeeder {

    private final TaskRepository taskRepository;

    @Value("${seedDatabase}")
    private boolean seedDatabase;
    public TaskDatabaseSeeder(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void seed() {
        if(!seedDatabase) {
            return;
        }

        List<String> tasks = new ArrayList<>(Arrays.asList(
                "Pet a cat",
                "Go for a walk",
                "Learn spanish",
                "Play some video games",
                "Go shopping"
        ));

        for(String task : tasks) {
            taskRepository.save(new Task(task, getRandomBoolean()));
        }
    }

    private boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }
}
