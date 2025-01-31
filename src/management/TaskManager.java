package management;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    // Обращение к методу менеджера истории
    List<Task> getHistoryManager();

    // Получение списка задач (a)
    ArrayList<Task> getTasks();

    // Получение списка эпиков (a)
    ArrayList<Epic> getEpics();

    // Получение списка подзадач (a)
    ArrayList<SubTask> getSubTasks();

    // Удаление всех задач (b)
    void clearTask();

    // Удаление всех эпиков (b)
    void clearEpic();

    // Удаление всех подзадач (b)
    void clearSubTask();

    // Получение по id задачи (c)
    Task getTask(int id);

    // Получение по id эпика (c)
    Epic getEpic(int id);

    // Получение по id подзадачи (c)
    SubTask getSubTask(int id);

    // Создание новой задачи (d)
    int addTask(Task newTask);

    // Создание нового эпика (d)
    int addEpic(Epic newEpic);

    // Создание новой подзадачи (d)
    int addSubTask(SubTask newSubTask);

    // Обновление задачи (e)
    void updateTask(Task updTask);

    // Обновление эпика (e)
    void updateEpic(Epic updEpic);

    // Обновление подзадачи (e)
    void updateSubTask(SubTask updSubTask);

    // Удаление задачи по id (f)
    void removeTask(int id);

    // Удаление эпика по id (f)
    void removeEpic(int id);

    // Удаление подзадачи по id (f)
    void removeSubTask(int id);

    // Получение подзадач для заданного по id эпика
    ArrayList<SubTask> getEpicSubTasks(int id);

    // Обновление статуса эпика
    void updateEpicStatus(int epicId);
}
