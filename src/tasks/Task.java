package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String taskName; // Название задачи
    private String taskDescription; // Описание задачи
    private int id; // Идентификатор задачи
    private Status status; // Статус задачи
    private LocalDateTime startTime; // Время начала задачи
    private Duration duration = Duration.ZERO; // Продолжительность задачи по умолчанию ноль

    // Конструктор для Задачи
    public Task(String taskName, String taskDescription, Status status) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = status;
    }

    // Конструктор для Эпика
    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }

    // Конструктор для FileBackedTaskManager
    public Task(String taskName, String taskDescription, Status status, int id) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = status;
        this.id = id;
    }

    // Конструктор со временем начала и продолжительностью
    public Task(String taskName, String taskDescription, Status status, LocalDateTime startTime, Duration duration) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    // Конструктор для FileBackedTaskManager со временем
    public Task(String taskName, String taskDescription, Status status, LocalDateTime startTime, Duration duration,
                int id) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.id = id;
    }

    // Конструктор для тестов эпика
    public Task(String taskName, String taskDescription, int id) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.id = id;
    }

    // Расчёт и получение времени окончания задачи
    public LocalDateTime getEndTime() {
        if (startTime == null) return null;
        return startTime.plus(duration);
    }

    // Получение типа задачи
    public TaskType getType() {
        return TaskType.TASK;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(id, otherTask.id);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        String result = "\"" + taskName + "\", ";
        if (taskDescription != null) {
            result = result + "описание: " + taskDescription.length() + " симв., ";
        } else {
            result = result + "Описание отсутствует, ";
        }
        result = result + "статус = " + status;
        result = result + ", начало: " + startTime;
        result = result + ", продолжительность: " + duration;
        result = result + ", id = " + id + ". ";
        return result;
    }
}