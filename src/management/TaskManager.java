package management;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    // Обращение к методу менеджера истории
    List<Task> getHistoryManager();

    // Получение списка задач
    ArrayList<Task> getTasks();

    // Получение списка эпиков
    ArrayList<Epic> getEpics();

    // Получение списка подзадач
    ArrayList<SubTask> getSubTasks();

    // Удаление всех задач
    void clearTask();

    // Удаление всех эпиков
    void clearEpic();

    // Удаление всех подзадач
    void clearSubTask();

    // Получение по id задачи
    Task getTask(int id);

    // Получение по id эпика
    Epic getEpic(int id);

    // Получение по id подзадачи
    SubTask getSubTask(int id);

    // Создание новой задачи
    int addTask(Task newTask);

    // Создание нового эпика
    int addEpic(Epic newEpic);

    // Создание новой подзадачи
    int addSubTask(SubTask newSubTask);

    // Обновление задачи
    void updateTask(Task updTask);

    // Обновление эпика
    void updateEpic(Epic updEpic);

    // Обновление подзадачи
    void updateSubTask(SubTask updSubTask);

    // Удаление задачи по id
    void removeTask(int id);

    // Удаление эпика по id
    void removeEpic(int id);

    // Удаление подзадачи по id
    void removeSubTask(int id);

    // Получение подзадач для заданного по id эпика
    ArrayList<SubTask> getEpicSubTasks(int id);

    // Обновление статуса эпика
    void updateEpicStatus(int epicId);

    // Расчёт и обновление времени начала эпика
    void updateEpicStartTime(int epicId);

    // Расчёт и обновление времени окончания эпика
    void updateEpicEndTime(int epicId);

    // Расчёт и обновление продолжительности эпика
    void updateEpicDurationTime(int epicId);

    // Проверка на пересечения
    void checkIntersections(Task task);

    // Возвращение списка задач, отсортированных по времени
    List<Task> getPrioritizedTasks();
}