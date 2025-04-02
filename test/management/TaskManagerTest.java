package management;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskManagerTest {

    private TaskManager manager = new InMemoryTaskManager();

    @Test // Объект Epic нельзя добавить в самого себя в виде подзадачи;
    public void epicCanNotYourselfSubtask() {
        // Подготовка: создание эпика и добавление его в мапу
        Epic epic1 = new Epic("Epic #1", "Epic #1 description");
        final int epicId1 = manager.addEpic(epic1);
        // Создание подзадачи с id(подзадачи) = id(эпика) (при равенстве id это один и тот же объект)
        SubTask subTask11 = new SubTask("SubTask #11", "", Status.NEW, epicId1, epicId1);
        final int subTaskId11 = manager.addSubTask(subTask11);

        // Проверка. Если id не равны, значит это разные объекты
        assertNotEquals(epic1.getId(), subTask11.getId(), "Эпик можно добавить в самого себя как подзадачу");
    }

    /*Подзадача не может стать своим эпиком. Тут только, если пользователь ошибётся и не верно вручную введёт
    номер эпика. Но про защиту от неправильного ввода id эпика для подзадачи не сказано.*/

    @Test // Утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров
    public void managerMakeCorrectManager() {
        // Создание менеджеров задач и истории через Managers
        TaskManager manager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        // Проверка
        assertNotNull(manager, "Утилитарный класс не верно возвращает менеджер задач");
        assertNotNull(historyManager, "Утилитарный класс не верно возвращает менеджер истории");
    }

    @Test //  InMemoryTaskManager добавляет задачи и может найти их по id
    public void addTaskAndFinedById() {
        // Подготовка. Создание и добавление одной задачи
        Task task1 = new Task("Task #1", "Task #1 description", Status.NEW);
        final int taskId1 = manager.addTask(task1);

        // Проверка
        assertNotNull(manager.getTask(taskId1), "Задача со сгенерированным id не найдена");
        assertEquals(1, manager.getTasks().size(), "В списке задач не одна задача");
    }

    @Test //  InMemoryTaskManager добавляет эпики и подзадачи и может найти их по id
    public void addTaskHeirAndFinedById() {
        // Подготовка. Создание и добавление одного эпика и двух подзадач для него
        Epic epic1 = new Epic("Epic #1", "Epic #1 description");
        final int epicId1 = manager.addEpic(epic1);
        SubTask subTask11 = new SubTask("SubTask #11", "", Status.NEW, epicId1);
        SubTask subTask12 = new SubTask("SubTask #12", "", Status.NEW, epicId1);
        final int subTaskId11 = manager.addSubTask(subTask11);
        final int subTaskId12 = manager.addSubTask(subTask12);

        // Проверка
        assertNotNull(manager.getEpic(epicId1), "Эпик со сгенерированным id не найден");
        assertNotNull(manager.getSubTask(subTaskId11), "Подзадача1 со сгенерированным id не найдена");
        assertNotNull(manager.getSubTask(subTaskId12), "Подзадача2 со сгенерированным id не найдена");
        assertEquals(1, manager.getEpics().size(), "В списке эпиков не один эпик");
        assertEquals(2, manager.getSubTasks().size(), "В списке подзадач не две подзадачи");
    }

    @Test // задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера
    public void doNotConflictTaskById() {
        // Подготовка. Для первой задачи генерирую id, для второй задаю вручную (причём равный) и добавляю в список задач
        Task task1 = new Task("Task #1", "Task #1 description", Status.NEW);
        final int taskId1 = manager.addTask(task1);
        Task task2 = new Task("Task #2", "Task #2 description", Status.NEW, taskId1);
        final int taskId2 = manager.addTask(task2);

        // Проверка.
        assertNotEquals(task1, task2, "Если id задать вручную, то в менеджере будет конфликт");
    }

    @Test // Неизменность задачи (по всем полям) при добавлении задачи в менеджер
    public void equalsTaskAllFieldAfterAddManager() {
        // Подготовка. Создание задачи со всеми полями кроме id и добавление её в менеджер
        Task task1 = new Task("Task #1", "Description", Status.NEW);
        final int taskId1 = manager.addTask(task1);

        // Проверка
        assertEquals("Task #1", manager.getTask(taskId1).getTaskName(), "Имя изменилось");
        assertEquals("Description", manager.getTask(taskId1).getTaskDescription(), "Описание изменилось");
        assertEquals(Status.NEW, manager.getTask(taskId1).getStatus(), "Статус изменился");
    }
}
