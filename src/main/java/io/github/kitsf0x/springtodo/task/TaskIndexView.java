package io.github.kitsf0x.springtodo.task;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route
public class TaskIndexView extends VerticalLayout {

    private final TaskRepository taskRepository;
    private final Grid<Task> grid = new Grid<>(Task.class);
    private final Button taskCreateViewRedirectButton = new Button("Create new task");

    @Autowired
    public TaskIndexView(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;

        grid.setColumns("id", "content", "done");

        grid.addComponentColumn(task -> createEditButton(task.getId()))
                .setHeader("Edit");

        List<Task> tasks = (List<Task>) taskRepository.findAll();
        grid.setItems(tasks);

        taskCreateViewRedirectButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        taskCreateViewRedirectButton.addClickListener(event -> UI.getCurrent().navigate(TaskView.class));

        add(grid);
        add(taskCreateViewRedirectButton);
    }

    private Button createEditButton(long taskId) {
        Button editButton = new Button("Edit");
        editButton.addClickListener(event -> UI.getCurrent().navigate("task/" + taskId));
        return editButton;
    }
}

