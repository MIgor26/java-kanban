package tasks;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks = new ArrayList<>(); // Список id подзадач

    // Конструктор для эпика
    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
        super.setStatus(Status.NEW);
    }

    // Конструктор для тестов
    public Epic(String taskName, String taskDescription, int id) {
        super(taskName, taskDescription, id);
    }

    // Конструктор для FileBackedTaskManager
    public Epic(String taskName, String taskDescription, Status status, int id) {
        super(taskName, taskDescription, status, id);
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    // Возвращение списка подзадач для эпика
    public ArrayList<SubTask> getEpicSubTasks() {
        return subTasks;
    }

    // Возвращение id подзадач для эпика
    public ArrayList<Integer> getSubTaskIds() {
        ArrayList<Integer> listIdSubTask = new ArrayList<>();
        for (int i = 0; i < subTasks.size(); i++) {
            listIdSubTask.add(subTasks.get(i).getId());
        }
        return listIdSubTask;
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

    // Установка статуса эпика
    @Override
    public void setStatus(Status status) {
        super.setStatus(status); //??Не помню зачем переопределил данный метод
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
        result = result + ", id = " + super.getId();
        return result;
    }
}
