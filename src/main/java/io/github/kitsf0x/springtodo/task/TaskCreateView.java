package io.github.kitsf0x.springtodo.task;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route
public class TaskCreateView extends VerticalLayout {
    private final TaskRepository taskRepository;
    private final TextField contentTextField = new TextField("Task title");
    private final Checkbox doneCheckbox = new Checkbox("Is done");
    private final Button addTaskButton = new Button("Create");
    public TaskCreateView(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;


        addTaskButton.addClickListener(buttonClickEvent -> createTaskFromFormInputAndSaveInDatabase());

        add(contentTextField);
        add(doneCheckbox);
        add(addTaskButton);
    }

    private void createTaskFromFormInputAndSaveInDatabase() {
        try {
            taskRepository.save(new Task(contentTextField.getValue(), doneCheckbox.getValue()));
        } catch(Exception ex) {
            Notification notification = Notification.show("Could not create new task!(" + ex.getMessage() + ")");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        contentTextField.setValue("");
        doneCheckbox.setValue(false);

        Notification notification = Notification.show("Created new task!");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}

