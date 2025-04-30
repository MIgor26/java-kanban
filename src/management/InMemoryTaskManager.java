package management;

import exception.TaskNotFoundException;
import exception.ValidateException;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected int countId = 0; // Счётчик id
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    // Доступ к HistoryManager без TaskManager
    public List<Task> getHistoryManager() {
        return historyManager.getHistory();
    }

    // Получение списка задач
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    // Получение списка эпиков
    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    // Получение списка подзадач
    @Override
    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    // Удаление всех задач
    @Override
    public void clearTask() {
        for (Task task : tasks.values()) { // Удаление всех задач из истории просмотра
            historyManager.remove(task.getId());
            prioritizedTasks.remove(task);
        }
        tasks.clear();
    }

    // Удаление всех эпиков
    @Override
    public void clearEpic() {
        for (Epic epic : epics.values()) { // Удаление всех эпиков из истории просмотра
            historyManager.remove(epic.getId());
        }
        epics.clear();
        for (SubTask subTask : subTasks.values()) { // Удаление всех подзадач из истории просмотра
            historyManager.remove(subTask.getId());
            prioritizedTasks.remove(subTask); // Удаление всех подзадач из приоритетного списка
        }
        subTasks.clear(); // Также удаляются все подзадачи
    }

    // Удаление всех подзадач
    @Override
    public void clearSubTask() {
        for (SubTask subTask : subTasks.values()) { // Удаление всех подзадач из истории просмотра
            historyManager.remove(subTask.getId());
            prioritizedTasks.remove(subTask); // Удаление всех подзадач из приоритетного списка
        }
        subTasks.clear();
        // Цикл для корректировки эпика
        for (Epic epic : epics.values()) {
            epic.clearEpicSubTask(); // удаление для всех эпиков списков подзадач
            epic.setStatus(Status.NEW); // Присвоение всем эпикам статуса NEW
            epic.setStartTime(null); // Установка времени начала эпика null
            epic.setEndTime(null); // Установка времени окончания эпика null
            epic.setDuration(Duration.ZERO); // Установка нулевой продолжительности эпика
        }
    }

    // Получение по id задачи
    @Override
    public Task getTask(int id) {
        if (tasks.get(id) == null) {
            String errorMessage = String.format("Задача с id %d не найдена.", id);
            throw new TaskNotFoundException(errorMessage);
        }
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    // Получение по id эпика
    @Override
    public Epic getEpic(int id) {
        if (epics.get(id) == null) {
            String errorMessage = String.format("Эпик с id %d не найден.", id);
            throw new TaskNotFoundException(errorMessage);
        }
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    // Получение по id подзадачи
    @Override
    public SubTask getSubTask(int id) {
        if (subTasks.get(id) == null) {
            String errorMessage = String.format("Подзадача с id %d не найдена.", id);
            throw new TaskNotFoundException(errorMessage);
        }
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    // Создание новой задачи
    @Override
    public int addTask(Task newTask) throws ValidateException {
        if (newTask.getStartTime() != null) { // Если время задано, то проверяем пересечения
            checkIntersections(newTask); // Проверка на пересечения
            prioritizedTasks.add(newTask);
        }
        countId++;
        newTask.setId(countId);
        tasks.put(countId, newTask);
        return countId;
    }

    // Создание нового эпика
    @Override
    public int addEpic(Epic newEpic) {
        countId++;
        newEpic.setId(countId);
        epics.put(countId, newEpic);
        return countId;
    }

    // Создание новой подзадачи
    @Override
    public int addSubTask(SubTask newSubTask) throws ValidateException {
        if (newSubTask.getStartTime() != null) { // Если время задано, то проверяем пересечения
            checkIntersections(newSubTask); // Проверка на пересечения
            prioritizedTasks.add(newSubTask);
        }
        countId++;
        newSubTask.setId(countId);
        subTasks.put(countId, newSubTask);
        epics.get(newSubTask.getEpicId()).putEpicSubTasks(newSubTask); // Добавление подзадачи в список эпика
        updateEpicStatus(newSubTask.getEpicId()); // Обновление статуса эпика к которому относится подзадача
        updateEpicTime(newSubTask.getEpicId()); // Обновление временных показателей эпика
        return countId;
    }

    // Обновление задачи
    @Override
    public void updateTask(Task updTask) throws ValidateException {
        if (updTask.getStartTime() != null) { // Если время задано, то проверяем пересечения
            checkIntersections(updTask); // Проверка на пересечения
            Task task = tasks.get(updTask.getId());
            prioritizedTasks.remove(task);
            prioritizedTasks.add(updTask);
        }
        tasks.put(updTask.getId(), updTask);
    }

    // Обновление эпика
    @Override
    public void updateEpic(Epic updEpic) {
        epics.put(updEpic.getId(), updEpic);
    }

    // Обновление подзадачи
    @Override
    public void updateSubTask(SubTask updSubTask) throws ValidateException {
        if (updSubTask.getStartTime() != null) { // Если время задано, то проверяем пересечения
            checkIntersections(updSubTask); // Проверка на пересечения
            SubTask subTask = subTasks.get(updSubTask.getId());
            prioritizedTasks.remove(subTask);
            prioritizedTasks.add(updSubTask);
        }
        subTasks.put(updSubTask.getId(), updSubTask);
        updateEpicStatus(updSubTask.getEpicId()); // Обновление статуса эпика к которому относится подзадача
        updateEpicTime(updSubTask.getEpicId()); // Обновление временных показателей эпика
    }

    // Удаление задачи по id
    @Override
    public void removeTask(int id) {
        prioritizedTasks.remove(tasks.get(id));
        historyManager.remove(id);
        tasks.remove(id);
    }

    // Удаление эпика по id
    @Override
    public void removeEpic(int id) {
        historyManager.remove(id);
        final Epic epic = epics.remove(id);
        for (Integer subtaskId : epic.getSubTaskIds()) {
            historyManager.remove(subtaskId); // Удаление Подзадач из истории при удалении Эпика
            subTasks.remove(subtaskId);
            for (Task task : getPrioritizedTasks()) {
                TaskType type = task.getType();
                if (type == TaskType.SUB_TASK) {
                    SubTask subTask = (SubTask) task;
                    if (subTask.getEpicId() == id) {
                        prioritizedTasks.remove(subTask);
                    }
                }
            }
        }
    }

    // Удаление подзадачи по id
    @Override
    public void removeSubTask(int id) {
        if (subTasks.get(id).getStartTime() != null) {
            prioritizedTasks.remove(subTasks.get(id));
        }
        historyManager.remove(id);
        int epicId = subTasks.get(id).getEpicId(); // Получение id эпика
        subTasks.remove(id);
        epics.get(epicId).removeEpicSubTask(id); // Удаление подзадачи в поле эпика
        updateEpicStatus(epicId); // Обновление статуса эпика к которому относится подзадача
        updateEpicTime(epicId); // Обновление временных показателей эпика
    }

    // Получение подзадач для заданного по id эпика
    @Override
    public ArrayList<SubTask> getEpicSubTasks(int id) { // на вход поступает id эпика
        if (epics.get(id) == null) {
            return new ArrayList<>();
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
        int counterDone = 0; // Счётчик статусов подзадач со статусом DONE
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

    // Расчёт и обновление времени начала, окончания и продолжительности эпика
    @Override
    public void updateEpicTime(int epicId) {
        if (getEpicSubTasks(epicId).isEmpty()) {
            epics.get(epicId).setStartTime(null); // Подзадач нет или удалены -> время начала эпика null
            epics.get(epicId).setEndTime(null); // -> время окончания эпика null
            epics.get(epicId).setDuration(Duration.ZERO); // -> время продолжительности эпика ноль
            return;
        }
        LocalDateTime startTime = LocalDateTime.MAX;
        LocalDateTime endTime = LocalDateTime.MIN;
        Duration duration = Duration.ZERO;
        for (SubTask subTask : getEpicSubTasks(epicId)) {
            duration = duration.plus(subTask.getDuration()); // Можно без проверок на null, так как по умолчанию
            // продолжительность у всех задач ZERO
            if (subTask.getStartTime() != null && subTask.getStartTime().isBefore(startTime)) {
                startTime = subTask.getStartTime();
            }
            if (subTask.getEndTime() != null && subTask.getEndTime().isAfter(endTime)) {
                endTime = subTask.getEndTime();
            }
        }
        if (startTime == LocalDateTime.MAX) startTime = null; // Если не задано время начала у подзадач
        if (endTime == LocalDateTime.MIN) endTime = null; // Если не задано время окончания у подзадач
        epics.get(epicId).setStartTime(startTime);
        epics.get(epicId).setEndTime(endTime);
        epics.get(epicId).setDuration(duration);
    }

    // Проверка на пересечения
    @Override
    public void checkIntersections(Task task) {
        List<Task> prioritizedTasks = getPrioritizedTasks();
        for (Task prioritizedTask : prioritizedTasks) {
            // Для случая обновления задачи, чтобы не проверять на пересечение с самой собой
            if (prioritizedTask.getId() == task.getId()) {
                continue;
            }
            if (task.getStartTime().isBefore(prioritizedTask.getEndTime())
                    && task.getEndTime().isAfter(prioritizedTask.getStartTime())) {
                throw new ValidateException("Пересечение задачи " + task.getTaskName() + " с "
                        + prioritizedTask.getTaskName());
            }
        }
    }

    // Возвращение списка задач, отсортированных по времени
    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }
}