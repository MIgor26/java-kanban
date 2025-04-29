package task;

import management.InMemoryTaskManager;
import management.Managers;
import management.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test // Экземпляры класса Task равны друг другу, если равен их id
    public void equalOfTasksById() {
        // Подготовка. У такс1 и таск2 равные id
        Task task1 = new Task("Task #1", "Task #1 description", Status.NEW, 1);
        Task task2 = new Task("Task #2", "Task #2 description", Status.NEW, 1);

        // Проверка
        assertEquals(task1, task2, "При равных id задачи разные");
    }

    @Test // Экземпляры наследники класса Task равны друг другу, если равен их id
    public void equalOfHeirById() {
        // Подготовка. У эпик1 и эпик2 равные id
        Epic epic1 = new Epic("Epic #1", "Epic #1 description", 1);
        Epic epic2 = new Epic("Epic #2", "Epic #2 description", 1);

        // Подготовка. У подзадачи1_1 и подзадачи1_2 равные id
        SubTask subTask11 = new SubTask("SubTask #11", "", Status.NEW, 2, 1);
        SubTask subTask12 = new SubTask("SubTask #12", "", Status.NEW, 2, 1);

        // Проверка
        assertEquals(epic1, epic2, "При равных Id эпики разные");
        assertEquals(subTask11, subTask12, "При равных Id подзадачи разные");
    }

    @Test // Статус Эпика NEW, если все подзадачи со статусом NEW
    public void boundaryConditionsStatusSubTaskNew() {
        // Подготовка
        SubTask subTask11 = new SubTask("SubTask #11", "", Status.NEW, 1);
        SubTask subTask12 = new SubTask("SubTask #12", "", Status.NEW, 1);
        Epic epic1 = new Epic("Epic #1", "Epic #1 description");
        TaskManager manager = Managers.getDefault();
        int idEpic1 = manager.addEpic(epic1);
        int idSubTask11 = manager.addSubTask(subTask11);
        int idSubTask12 = manager.addSubTask(subTask12);

        // Проверка
        assertEquals(epic1.getStatus(), Status.NEW, "У эпика нет статуса NEW, когда все подзадачи со статусом NEW");
    }

    @Test // Статус Эпика NEW, если все подзадачи со статусом NEW
    public void boundaryConditionsStatusSubTaskDone() {
        // Подготовка
        SubTask subTask11 = new SubTask("SubTask #11", "", Status.DONE, 1);
        SubTask subTask12 = new SubTask("SubTask #12", "", Status.DONE, 1);
        Epic epic1 = new Epic("Epic #1", "Epic #1 description");
        TaskManager manager = Managers.getDefault();
        int idEpic1 = manager.addEpic(epic1);
        int idSubTask11 = manager.addSubTask(subTask11);
        int idSubTask12 = manager.addSubTask(subTask12);

        // Проверка
        assertEquals(epic1.getStatus(), Status.DONE, "У эпика нет статуса NEW, когда все подзадачи со статусом NEW");
    }

    @Test // Статус Эпика NEW, если все подзадачи со статусом NEW
    public void epicStatusInProgressWithDifferentStatuseSubTask() {
        // Подготовка
        SubTask subTask11 = new SubTask("SubTask #11", "", Status.DONE, 1);
        SubTask subTask12 = new SubTask("SubTask #12", "", Status.NEW, 1);
        Epic epic1 = new Epic("Epic #1", "Epic #1 description");
        TaskManager manager = Managers.getDefault();
        int idEpic1 = manager.addEpic(epic1);
        int idSubTask11 = manager.addSubTask(subTask11);
        int idSubTask12 = manager.addSubTask(subTask12);

        // Проверка
        assertEquals(epic1.getStatus(), Status.IN_PROGRESS, "У эпика нет статуса NEW, когда все подзадачи со статусом NEW");
    }
}
