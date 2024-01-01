package io.github.kitsf0x.springtodo.task;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import java.util.Optional;

@Route("task/:id?")
public class TaskView extends VerticalLayout implements BeforeEnterObserver{
    private final TaskRepository taskRepository;
    private final TextField contentTextField = new TextField("Task title");
    private final Checkbox doneCheckbox = new Checkbox("Is done");
    private final Button addTaskButton = new Button();
    private long id;
    private boolean editMode;
    public TaskView(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;


        addTaskButton.addClickListener(buttonClickEvent -> save());


        add(contentTextField);
        add(doneCheckbox);
        add(addTaskButton);
    }

    private void save() {
        Task task = new Task(contentTextField.getValue(), doneCheckbox.getValue());
        if (editMode) {
            task.setId(id);
        }
        try {
            taskRepository.save(task);
        } catch (Exception ex) {
            Notification notification = Notification.show("Could not create new task!(" + ex.getMessage() + ")");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        contentTextField.setValue("");
        doneCheckbox.setValue(false);

        Notification notification = Notification.show(getNorificationString());
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        UI.getCurrent().navigate(TaskIndexView.class);
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        final Optional<String> optionalId = event.getRouteParameters().get("id");
        optionalId.ifPresentOrElse(this::switchToEditMode, this::switchToCreateMode);
        addTaskButton.setText(getSubmitButtonString());

    }

    private void switchToEditMode(String id) {
        this.editMode = true;
        this.id = Long.parseLong(id);

        taskRepository.findById(this.id).ifPresent(task -> {
            contentTextField.setValue(task.getContent());
            doneCheckbox.setValue(task.isDone());
        });
    }

    private void switchToCreateMode() {
        this.editMode = false;

    }

    private String getNorificationString() {
        if(editMode) {
            return "Task has been updated!";
        }
        else {
            return "Task has been created!";
        }
    }

    private String getSubmitButtonString() {
        if(editMode) {
            return "Update";
        }
        else {
            return "Create";
        }
    }

}

