package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks = new ArrayList<>(); // Список id подзадач
    private LocalDateTime endTime;

    // Конструктор для эпика
    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
        super.setStatus(Status.NEW);
        super.setDuration(Duration.ZERO);
    }

    // Конструктор для FileBackedTaskManager
    public Epic(String taskName, String taskDescription, Status status, int id) {
        super(taskName, taskDescription, status, id);
    }

    // Конструктор для FileBackedTaskManager со временем
    public Epic(String taskName, String taskDescription, Status status, LocalDateTime startTime, Duration duration,
                LocalDateTime endTime, int id) {
        super(taskName, taskDescription, status, startTime, duration, id);
        this.endTime = endTime;
    }

    // Конструктор для тестов
    public Epic(String taskName, String taskDescription, int id) {
        super(taskName, taskDescription, id);
    }

    // Получение типа Эпика
    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    // Переопределение получения времени окончания эпика
    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    // Возвращение списка подзадач для эпика
    public ArrayList<SubTask> getEpicSubTasks() {
        return subTasks;
    }

    // Возвращение id подзадач для эпика
    public List<Integer> getSubTaskIds() {
        List<Integer> listSubTuskId = subTasks.stream().map(Task::getId).toList(); // ?? Тут сомневаюсь не нарушен ли принцип неизменности?
        return new ArrayList<>(listSubTuskId);
    }

    // Изменение времени окончания эпика
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    // Добавление подзадач для эпика в список
    public void putEpicSubTasks(SubTask subTask) {
        subTasks.add(subTask);
    }

    // Удаление подзадач из списка эпика
    public void removeEpicSubTask(int id) {
        subTasks.remove(id);
    }

    // Удаление всех подзадач из списка эпика
    public void clearEpicSubTask() {
        subTasks.clear();
    }

    // Вывод на печать наименование эпика, кол-во символов описания и количество подзадач
    @Override
    public String toString() {
        String result = "\"" + super.getTaskName() + "\", ";
        if (super.getTaskDescription() != null) {
            result = result + "описание: " + super.getTaskDescription().length() + " симв., ";
        } else {
            result = result + "описание отсутствует, ";
        }
        if (subTasks != null) {
            result = result + "кол-во подзадач = " + subTasks.size() + ", ";
        } else {
            result = result + "подзадачи отсутствуют, ";
        }
        result = result + "статус = " + super.getStatus();
        result = result + ", начало: " + super.getStartTime();
        result = result + ", продолжительность: " + super.getDuration();
        result = result + ", id = " + super.getId();
        return result;
    }
}
