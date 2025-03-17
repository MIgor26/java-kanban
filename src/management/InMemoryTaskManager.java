package management;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>(); // Мапа с задачами
    private HashMap<Integer, Epic> epics = new HashMap<>(); // Мапа с эпиками
    private HashMap<Integer, SubTask> subTasks = new HashMap<>(); // Мапа с подзадачами
    private int countId = 0; // Счётчик id
    private HistoryManager historyManager = Managers.getDefaultHistory();

    // Доступ к HistoryManager без TaskManager
    public List<Task> getHistoryManager() {
        return historyManager.getHistory();
    }

    // Получение списка задач (a)
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    // Получение списка эпиков (a)
    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    // Получение списка подзадач (a)
    @Override
    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    // Удаление всех задач (b)
    @Override
    public void clearTask() {
        for (Task task : tasks.values()) { // Удаление всех задач из истории просмотра
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    // Удаление всех эпиков (b)
    @Override
    public void clearEpic() {
        for (Epic epic : epics.values()) { // Удаление всех эпиков из истории просмотра
            historyManager.remove(epic.getId());
        }
        epics.clear();
        for (SubTask subTask : subTasks.values()) { // Удаление всех подзадач из истории просмотра
            historyManager.remove(subTask.getId());
        }
        subTasks.clear(); // Также удаляются все подзадачи
    }

    // Удаление всех подзадач (b)
    @Override
    public void clearSubTask() {
        for (SubTask subTask : subTasks.values()) { // Удаление всех подзадач из истории просмотра
            historyManager.remove(subTask.getId());
        }
        subTasks.clear();
        for (Integer id : epics.keySet()) {
            epics.get(id).setStatus(Status.NEW);// Присваивается статус NEW всем эпикам
        }
        for (Epic epic : epics.values()) { // Удаление для всех эпиков мап с подзадачами
            epic.clearEpicSubTask();
        }
    }

    // Получение по id задачи (c)
    @Override
    public Task getTask(int id) {
        if (tasks.get(id) != null) {
            historyManager.add(tasks.get(id));
        }
        return tasks.get(id);
    }

    // Получение по id эпика (c)
    @Override
    public Epic getEpic(int id) {
        if (epics.get(id) != null) {
            historyManager.add(epics.get(id));
        }
        return epics.get(id);
    }

    // Получение по id подзадачи (c)
    @Override
    public SubTask getSubTask(int id) {
        if (subTasks.get(id) != null) {
            historyManager.add(subTasks.get(id));
        }
        return subTasks.get(id);
    }

    // Создание новой задачи (d)
    @Override
    public int addTask(Task newTask) {
        countId++;
        newTask.setId(countId);
        tasks.put(countId, newTask);
        return countId;
    }

    // Создание нового эпика (d)
    @Override
    public int addEpic(Epic newEpic) {
        countId++;
        newEpic.setId(countId);
        epics.put(countId, newEpic);
        return countId;
    }

    // Создание новой подзадачи (d)
    @Override
    public int addSubTask(SubTask newSubTask) {
        countId++;
        newSubTask.setId(countId);
        subTasks.put(countId, newSubTask);
        epics.get(newSubTask.getEpicId()).putEpicSubTasks(newSubTask); // Добавление подзадачи в список эпика
        updateEpicStatus(newSubTask.getEpicId()); // Обновление статуса эпика к которому относится подзадача
        return countId;
    }

    // Обновление задачи (e)
    @Override
    public void updateTask(Task updTask) {
        tasks.put(updTask.getId(), updTask);
    }

    // Обновление эпика (e)
    @Override
    public void updateEpic(Epic updEpic) {
        epics.put(updEpic.getId(), updEpic);
    }

    // Обновление подзадачи (e)
    @Override
    public void updateSubTask(SubTask updSubTask) {
        subTasks.put(updSubTask.getId(), updSubTask);
        updateEpicStatus(updSubTask.getEpicId()); // Обновление статуса эпика к которому относится подзадача
    }

    // Удаление задачи по id (f)
    @Override
    public void removeTask(int id) {
        historyManager.remove(id);
        tasks.remove(id);
    }

    // Удаление эпика по id (f)
    @Override
    public void removeEpic(int id) {
        historyManager.remove(id);
        final Epic epic = epics.remove(id); // ??Не помню, почему final
        for (Integer subtaskId : epic.getSubTaskIds()) {
            historyManager.remove(subtaskId); // Удаление Субтасков из истории при удалении Эпика
            subTasks.remove(subtaskId);
        }
    }

    // Удаление подзадачи по id (f)
    @Override
    public void removeSubTask(int id) {
        historyManager.remove(id);
        int epicId = subTasks.get(id).getEpicId(); // Получение id эпика, т. к. после удаления подзадачи его не получишь
        subTasks.remove(id);
        epics.get(epicId).removeEpicSubTask(id); // Удаление подзадачи в поле эпика
        updateEpicStatus(epicId); // Обновление статуса эпика к которому относится подзадача
    }

    // Получение подзадач для заданного по id эпика
    @Override
    public ArrayList<SubTask> getEpicSubTasks(int id) { // на вход поступает id эпика
        if (epics.get(id) == null) {
            return new ArrayList<SubTask>();
        }
        return epics.get(id).getEpicSubTasks(); // Возвращаем подзадачи для эпика
    }

    // Обновление статуса эпика
    @Override
    public void updateEpicStatus(int epicId) {
        if (getEpicSubTasks(epicId).isEmpty()) { // Проверка на наличие подзадач (рефакторинг)
            epics.get(epicId).setStatus(Status.NEW); // Если подзадач нет, то статус эпика = NEW
            return;
        }
        ArrayList<SubTask> tasksForEpic = getEpicSubTasks(epicId); // Получение всех подзадач для эпика
        int counterDone = 0; // Счётчик статусов подзадач со статусом DONE (не написал count, т.к. смешно читается :)
        int counterNew = 0; // Счётчик статусов подзадач со статусом NEW
        for (SubTask task : tasksForEpic) { // Перебор id подзадач эпика
            if (task.getStatus() == Status.DONE) { // Проверка является ли статус подзадачи DONE
                counterDone++;
            } else if (task.getStatus() == Status.NEW) {
                counterNew++;
            }
        }
        if (counterDone == tasksForEpic.size()) { // Если все подзадачи имеют статус DONE, то статус эпика = DONE
            epics.get(epicId).setStatus(Status.DONE);
        } else if (counterNew == tasksForEpic.size()) { // Если все подзадачи имеют статус NEW, то статус эпика = NEW
            epics.get(epicId).setStatus(Status.NEW);
        } else { // Во всех остальных случаях статус эпика = IN PROGRESS
            epics.get(epicId).setStatus(Status.IN_PROGRESS);
        }
    }
}
