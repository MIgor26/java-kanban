package management;

import exception.ManagerSaveException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest {

    @BeforeEach
    void initManager() {
        manager = getTaskManager();
    }

    @Override
    FileBackedTaskManager getTaskManager() {
        File file = null;
        try {
            file = File.createTempFile("file", ".scv");
        } catch (IOException e) {
            System.out.println(e);
        }
        return Managers.getDefaultFile(file);
    }

    // Сохранение нескольких задач в файл
    @Test
    public void saveTaskToFileTest() {
        // Создание нескольких задач с автосохранением их в файл
        Task task1 = new Task("Task #1", "Task #1 description", Status.NEW);
        final int taskId1 = manager.addTask(task1);
        Epic epic1 = new Epic("Epic #1", "Epic #1 description");
        final int epicId1 = manager.addEpic(epic1);
        SubTask subTask11 = new SubTask("SubTask #11", "", Status.NEW, epicId1);
        final int subTaskId11 = manager.addSubTask(subTask11);

        // Проверка правильности сохранения задач в файл
        assertEquals(manager.getTask(taskId1), task1, "Задача не эквивалентна");
        assertEquals(manager.getEpic(epicId1), epic1, "Эпик не эквивалентен");
        assertEquals(manager.getSubTask(subTaskId11), subTask11, "Подзадача не эквивалентна");
    }

    // Загрузка нескольких задач из файла
    @Test
    public void loadFromFileTest() throws IOException {
        File file = File.createTempFile("file", ".scv");
        FileBackedTaskManager manager1 = new FileBackedTaskManager(file);

        Task task1 = new Task("Task #1", "Task #1 description", Status.NEW);
        final int taskId1 = manager1.addTask(task1);
        Epic epic1 = new Epic("Epic #1", "Epic #1 description");
        final int epicId1 = manager1.addEpic(epic1);
        SubTask subTask11 = new SubTask("SubTask #11", "", Status.NEW, epicId1);
        final int subTaskId11 = manager1.addSubTask(subTask11);

        // Загрузка менеджера из файла
        TaskManager manager2 = FileBackedTaskManager.loadFromFile(manager1.getFile());

        // Проверка правильности загрузки задач из файла
        assertEquals(manager2.getTask(taskId1), task1, "Задача не эквивалентна");
        assertEquals(manager2.getEpic(epicId1), epic1, "Эпик не эквивалентен");
        assertEquals(manager2.getSubTask(subTaskId11), subTask11, "Подзадача не эквивалентна");
    }

    // Выброс исключения при обращении к несуществующему файлу
    @Test
    public void throwingExceptionWhenAccessingNonExistentFile() {
        ManagerSaveException thrown = Assertions.assertThrows(ManagerSaveException.class, () ->
                FileBackedTaskManager.loadFromFile(new File("fileNull", "csv")));
        Assertions.assertEquals("Ошибка при чтении из файла: ", thrown.getMessage());

    }
}
