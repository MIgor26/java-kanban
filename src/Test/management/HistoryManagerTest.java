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

    @Test // Задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных
    public void addTaskToHistoryManagerCorrect() {
        // Подготовка. Создание и добавление в мапу 2 задач
        Task task1 = new Task("Task #1", "Task #1 description", Status.NEW);
        Task task2 = new Task("Task #2", "Task #2 description", Status.IN_PROGRESS);
        final int taskId1 = manager.addTask(task1);
        final int taskId2 = manager.addTask(task2);

        // Вызов данных задач по id для сохранения в истории вызванных задач
        manager.getTask(taskId1);
        manager.getTask(taskId2);

        // Преобразование списков в массив
        List<Task> tl = manager.getTasks();
        Task[] tasks = new Task[tl.size()];
        tl.toArray(tasks);
        tl = manager.getHistoryManager();
        Task[] history = new Task[tl.size()];
        tl.toArray(history);

        // Проверка
        assertArrayEquals(tasks, history, "Задачи, добавленные в History Manager не сохраняют предыдущую версию задач");
    }

    @Test // В историю вызовов 10 последних задач не добавляется null
    public void notNullInVieved10Tasks() {
        // Подготовка
        Task task1 = new Task("Task #1", "Task #1 description", Status.NEW);
        final int taskId1 = manager.addTask(task1);
        // Вызов существующей задачи и несуществующих
        manager.getTask(taskId1);
        manager.getTask(2);
        manager.getSubTask(2);
        manager.getEpic(2);

        // Проверка
        assertEquals(1, manager.getHistoryManager().size(), "В историю добавлена нулевая задача");
    }

}
