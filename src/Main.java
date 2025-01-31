import management.HistoryManager;
import management.InMemoryTaskManager;
import management.Managers;
import management.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.sql.SQLOutput;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        // Создание 5-ти новых задач
        Task task1 = new Task("Task #1", "Task #1 description", Status.NEW);
        Task task2 = new Task("Task #2", "Task #2 description", Status.NEW);
        Task task3 = new Task("Task #3", "Task #3 description", Status.NEW);
        Task task4 = new Task("Task #4", "Task #4 description", Status.IN_PROGRESS);
        Task task5 = new Task("Task #5", "Task #5 description", Status.DONE);
        final int taskId1 = manager.addTask(task1);
        final int taskId2 = manager.addTask(task2);
        final int taskId3 = manager.addTask(task3);
        final int taskId4 = manager.addTask(task4);
        final int taskId5 = manager.addTask(task5);

        // Создание 3-х эпиков
        Epic epic1 = new Epic("Epic #1", "Epic #1 description");
        Epic epic2 = new Epic("Epic #2", "Epic #2 description");
        Epic epic3 = new Epic("Epic #3", "Epic #3 description");
        final int epicId1 = manager.addEpic(epic1);
        final int epicId2 = manager.addEpic(epic2);
        final int epicId3 = manager.addEpic(epic3);

        // Создание 3-х подзадач для каждого эпика
        SubTask subTask1_1 = new SubTask("SubTask #1_1", "", Status.NEW, epicId1);
        SubTask subTask1_2 = new SubTask("SubTask #1_2", "", Status.NEW, epicId1);
        SubTask subTask1_3 = new SubTask("SubTask #1_3", "", Status.NEW, epicId1);
        final int subTaskId1_1 = manager.addSubTask(subTask1_1);
        final int subTaskId1_2 = manager.addSubTask(subTask1_2);
        final int subTaskId1_3 = manager.addSubTask(subTask1_3);
        SubTask subTask2_1 = new SubTask("SubTask #2_1", "", Status.NEW, epicId2);
        SubTask subTask2_2 = new SubTask("SubTask #2_2", "", Status.NEW, epicId2);
        SubTask subTask2_3 = new SubTask("SubTask #2_3", "", Status.IN_PROGRESS, epicId2);
        final int subTaskId2_1 = manager.addSubTask(subTask2_1);
        final int subTaskId2_2 = manager.addSubTask(subTask2_2);
        final int subTaskId2_3 = manager.addSubTask(subTask2_3);
        SubTask subTask3_1 = new SubTask("SubTask #3_1", "", Status.NEW, epicId3);
        SubTask subTask3_2 = new SubTask("SubTask #3_2", "", Status.IN_PROGRESS, epicId3);
        SubTask subTask3_3 = new SubTask("SubTask #3_3", "", Status.DONE, epicId3);
        final int subTaskId3_1 = manager.addSubTask(subTask3_1);
        final int subTaskId3_2 = manager.addSubTask(subTask3_2);
        final int subTaskId3_3 = manager.addSubTask(subTask3_3);

        System.out.println("\n***");
        System.out.println("   Этап №1. Создание задач");
        System.out.println("Список задач: " + manager.getTasks());
        System.out.println("Список эпиков: " + manager.getEpics());
        System.out.println("Список подзадач: " + manager.getSubTasks());

        System.out.println("\n***\n   Тест истории просмотров");
        System.out.println("Задачи:");
        for (Task task: manager.getTasks()) {
            System.out.println(manager.getTask(task.getId()));
        }

        System.out.println(manager.getTask(taskId1));
        System.out.println(manager.getTask(taskId1));
        System.out.println(manager.getTask(taskId1));

        System.out.println("Эпики с подзадачами: ");
        for (Task epic : manager.getEpics()) {
            System.out.println(manager.getEpic(epic.getId()));
            System.out.println("включает подзадачи: " + manager.getEpicSubTasks(epic.getId()));
        }

        System.out.println("История:");
        for (Task task : manager.getHistoryManager()) {
            System.out.println(task);
        }
    }
}
