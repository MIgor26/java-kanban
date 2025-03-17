package Test.management;

import management.HistoryManager;
import management.Managers;
import management.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {

    HistoryManager historyManager = Managers.getDefaultHistory();
    TaskManager manager = Managers.getDefault();

    @Test
        // Задача, к которой происходит повторное обращение, перемещается списка
    void addTaskEndList() {
        // Создание задач
        Task task1 = new Task("Task #1", "Task #1 description", Status.NEW);
        manager.addTask(task1);
        Task task2 = new Task("Task #2", "Task #2 description", Status.NEW);
        manager.addTask(task2);
        Task task3 = new Task("Task #3", "Task #3 description", Status.NEW);
        manager.addTask(task3);
        // Обращение к задачам
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        manager.getTask(task3.getId());
        // Повторное обращение ко 2 задаче
        manager.getTask(task2.getId());

        // Проверка
        assertEquals(manager.getHistoryManager(), List.of(task1, task3, task2));

    }

    @Test
        // Удаляемая задача (не крайняя), удаляется из истории просмотров
    void deleteTaskFromHistoru() {
        // Создание задач
        Task task1 = new Task("Task #1", "Task #1 description", Status.NEW);
        manager.addTask(task1);
        Task task2 = new Task("Task #2", "Task #2 description", Status.NEW);
        manager.addTask(task2);
        Task task3 = new Task("Task #3", "Task #3 description", Status.NEW);
        manager.addTask(task3);
        // Обращение к задачам
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        manager.getTask(task3.getId());

        // Удаление 2 задачи (не крайней)
        manager.removeTask(task2.getId());
        // Проверка
        assertEquals(manager.getHistoryManager(), List.of(task1, task3));

        // Удаление первой задачи
        manager.removeTask(task1.getId());
        // Проверка
        assertEquals(manager.getHistoryManager(), List.of(task3));
    }

    @Test
        // Удаляемая крайняя задача удаляется из истории просмотров
    void deleteExtremeTaskFromHistoru() {
        // Создание задач
        Task task1 = new Task("Task #1", "Task #1 description", Status.NEW);
        manager.addTask(task1);
        Task task2 = new Task("Task #2", "Task #2 description", Status.NEW);
        manager.addTask(task2);
        Task task3 = new Task("Task #3", "Task #3 description", Status.NEW);
        manager.addTask(task3);
        // Обращение к задачам
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        manager.getTask(task3.getId());

        // Удаление первой и последней задачи
        manager.removeTask(task1.getId());
        manager.removeTask(task3.getId());
        // Проверка
        assertEquals(manager.getHistoryManager(), List.of(task2));
    }

}