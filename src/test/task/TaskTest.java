//package test.tasks;
//
//import org.junit.jupiter.api.Test;
//import tasks.Epic;
//import tasks.Status;
//import tasks.SubTask;
//import tasks.Task;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class TaskTest {
//
//    @Test // Экземпляры класса Task равны друг другу, если равен их id
//    public void equalOfTasksById() {
//        // Подготовка. У такс1 и таск2 равные id
//        Task task1 = new Task("Task #1", "Task #1 description", Status.NEW, 1);
//        Task task2 = new Task("Task #2", "Task #2 description", Status.NEW, 1);
//
//        // Проверка
//        assertEquals(task1, task2, "При равных id задачи разные");
//    }
//
//    @Test // Экземпляры наследники класса Task равны друг другу, если равен их id
//    public void equalOfHeirById() {
//        // Подготовка. У эпик1 и эпик2 равные id
//        Epic epic1 = new Epic("Epic #1", "Epic #1 description", 1);
//        Epic epic2 = new Epic("Epic #2", "Epic #2 description", 1);
//
//        // Подготовка. У подзадачи1_1 и подзадачи1_2 равные id
//        SubTask subTask11 = new SubTask("SubTask #1_1", "", Status.NEW, 2, 1);
//        SubTask subTask12 = new SubTask("SubTask #1_2", "", Status.NEW, 2, 1);
//
//        // Проверка
//        assertEquals(epic1, epic2, "При равных Id эпики разные");
//        assertEquals(subTask11, subTask12, "При равных Id подзадачи разные");
//    }
//
//}
