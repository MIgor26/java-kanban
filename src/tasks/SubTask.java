package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId; // Идентификатор эпика, к которому принадлежит подзадача

    // Конструктор для подзадачи
    public SubTask(String taskName, String taskDescription, Status status, int epicId) {
        super(taskName, taskDescription, status);
        this.epicId = epicId;
    }

    // Конструктор со временем начала и продолжительностью
    public SubTask(String taskName, String taskDescription, Status status, int epicId, LocalDateTime startTime,
                   Duration duration) {
        super(taskName, taskDescription, status, startTime, duration);
        this.epicId = epicId;
    }

    // Конструктор для FileBackedTaskManager
    public SubTask(String taskName, String taskDescription, Status status, int id, int epicId) {
        super(taskName, taskDescription, status, id);
        this.epicId = epicId;
    }

    // Конструктор для FileBackedTaskManager со временем
    public SubTask(String taskName, String taskDescription, Status status, LocalDateTime startTime, Duration duration,
                   int id, int epicId) {
        super(taskName, taskDescription, status, startTime, duration, id);
        this.epicId = epicId;
    }

    // Получение типа Подзадачи
    @Override
    public TaskType getType() {
        return TaskType.SUB_TASK;
    }

    // Доступ к id эпика для подзадачи
    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        String result = "\"" + super.getTaskName() + "\", ";
        if (super.getTaskDescription() != null) {
            result = result + "описание: " + super.getTaskDescription().length() + " симв., ";
        } else {
            result = result + "описание отсутствует, ";
        }
        result = result + "для эпика с id = " + epicId;
        result = result + ", статус = " + super.getStatus();
        result = result + ", начало: " + super.getStartTime();
        result = result + ", продолжительность: " + super.getDuration();
        result = result + ", id = " + super.getId();
        return result;
    }
}
