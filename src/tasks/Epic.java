package tasks;

import java.util.HashMap;
import java.util.ArrayList;

public class Epic extends Task {
    private HashMap<Integer, SubTask> subTasks = new HashMap<>(); // Список подзадач

    // Конструктор для эпика
    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
        super.setStatus(Status.NEW);
    }

    // Конструктор новый для тестов
    public Epic(String taskName, String taskDescription, int id) {
        super(taskName, taskDescription, id);
    }

    // Возвращение списка подзадач для эпика
    public ArrayList<SubTask> getEpicSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    // Возвращение id подзадач для эпика
    public ArrayList<Integer> getSubTaskIds() {
        return new ArrayList<>(subTasks.keySet());
    }

    // Добавление подзадач для эпика в список
    public void putEpicSubTasks(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
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
