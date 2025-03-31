package tasks;

import java.util.Objects;

public class Task {
    private String taskName; // Название задачи
    private String taskDescription; // Описание задачи
    private int id; // Идентификатор задачи
    private Status status; // Статус задачи

    // Конструктор для задачи
    public Task(String taskName, String taskDescription, Status status) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = status;
    }

    // Конструктор без изменения статуса
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

    // Конструктор новый для тестов эпика
    public Task(String taskName, String taskDescription, int id) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.id = id;
    }

    // Получение типа задачи
    public TaskType getType() {
        return TaskType.TASK;
    }

    // Доступ к имени задачи в других классах
    public String getTaskName() {
        return taskName;
    }

    // Инициализация имени задачи в других классах
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    // Доступ к описанию задачи в других классах
    public String getTaskDescription() {
        return taskDescription;
    }

    // Инициализация описания задачи в других классах
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    // Доступ к id в других классах
    public int getId() {
        return id;
    }

    // Инициализация id в других классах
    public void setId(int id) {
        this.id = id;
    }

    // Доступ к статусу в других классах
    public Status getStatus() {
        return status;
    }

    // Инициализация статуса в других классах
    public void setStatus(Status status) {
        this.status = status;;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return  true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(id, otherTask.id);
    }

    @Override
    public int hashCode() {
        int hash = id;
        return hash;
    }

    // Вывод на печать наименование задачи и кол-во символов описания
    @Override
    public String toString() {
        String result = "\"" + taskName + "\", ";
        if (taskDescription != null ) {
            result = result + "описание: " + taskDescription.length() + " симв., ";
        } else {
            result = result + "Описание отсутствует, ";
        }
        result = result + "статус = " + status;
        result = result + ", id = " + id + ". ";
        return result;
    }
}
