package management;

import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.File;
import java.io.IOException;

import static java.io.File.createTempFile;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    // Сохранение нескольких задач в файл
    @Test
    public void saveTaskToFileTest() throws IOException {

        // Подготовка файла
        File file = createTempFile("testTasks", ".scv");
        FileBackedTaskManager fileBackedTaskManager = Managers.getDefaultFile(file);
        // Создание нескольких задач с автосохранением их в файл
        Task task1 = new Task("Task #1", "Task #1 description", Status.NEW);
        final int taskId1 = fileBackedTaskManager.addTask(task1);
        Epic epic1 = new Epic("Epic #1", "Epic #1 description");
        final int epicId1 = fileBackedTaskManager.addEpic(epic1);
        SubTask subTask11 = new SubTask("SubTask #11", "", Status.NEW, epicId1);
        final int subTaskId11 = fileBackedTaskManager.addSubTask(subTask11);

        // Проверка правильности сохранения задач в файл
        assertEquals(fileBackedTaskManager.getTask(taskId1), task1, "Задача не эквивалентна");
        assertEquals(fileBackedTaskManager.getEpic(epicId1), epic1, "Эпик не эквивалентен");
        assertEquals(fileBackedTaskManager.getSubTask(subTaskId11), subTask11, "Подзадача не эквивалентна");
    }

    // Загрузка нескольких задач из файла
    @Test
    public void loadFromFileTest() throws IOException {

        // Подготовка файла с задачами
        File file = createTempFile("testTasks", ".scv");
        FileBackedTaskManager fileBackedTaskManager = Managers.getDefaultFile(file);
        Task task1 = new Task("Task #1", "Task #1 description", Status.NEW);
        final int taskId1 = fileBackedTaskManager.addTask(task1);
        Epic epic1 = new Epic("Epic #1", "Epic #1 description");
        final int epicId1 = fileBackedTaskManager.addEpic(epic1);
        SubTask subTask11 = new SubTask("SubTask #11", "", Status.NEW, epicId1);
        final int subTaskId11 = fileBackedTaskManager.addSubTask(subTask11);

        // Загрузка менеджера из файла
        FileBackedTaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(file);

        // Проверка правильности загрузки задач из файла
        assertEquals(fileBackedTaskManager1.getTask(taskId1), task1, "Задача не эквивалентна");
        assertEquals(fileBackedTaskManager1.getEpic(epicId1), epic1, "Эпик не эквивалентен");
        assertEquals(fileBackedTaskManager1.getSubTask(subTaskId11), subTask11, "Подзадача не эквивалентна");
    }
}
