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
        SubTask subTask11 = new SubTask("SubTask #11", "", Status.NEW, epicId1);
        SubTask subTask12 = new SubTask("SubTask #12", "", Status.NEW, epicId1);
        SubTask subTask13 = new SubTask("SubTask #13", "", Status.NEW, epicId1);
        final int subTaskId11 = manager.addSubTask(subTask11);
        final int subTaskId12 = manager.addSubTask(subTask12);
        final int subTaskId13 = manager.addSubTask(subTask13);
        SubTask subTask21 = new SubTask("SubTask #21", "", Status.NEW, epicId2);
        SubTask subTask22 = new SubTask("SubTask #22", "", Status.NEW, epicId2);
        SubTask subTask23 = new SubTask("SubTask #23", "", Status.IN_PROGRESS, epicId2);
        final int subTaskId21 = manager.addSubTask(subTask21);
        final int subTaskId22 = manager.addSubTask(subTask22);
        final int subTaskId23 = manager.addSubTask(subTask23);
        SubTask subTask31 = new SubTask("SubTask #31", "", Status.NEW, epicId3);
        SubTask subTask32 = new SubTask("SubTask #32", "", Status.IN_PROGRESS, epicId3);
        SubTask subTask33 = new SubTask("SubTask #33", "", Status.DONE, epicId3);
        final int subTaskId31 = manager.addSubTask(subTask31);
        final int subTaskId32 = manager.addSubTask(subTask32);
        final int subTaskId33 = manager.addSubTask(subTask33);

        System.out.println("    Этап №1. В менеджере задач хранятся следующие задачи, эпики и подзадачи");
        System.out.println("Список задач: " + manager.getTasks());
        System.out.println("Список эпиков: " + manager.getEpics());
        System.out.println("Список подзадач: " + manager.getSubTasks());

        System.out.println("\n***\n    Этап №2. Обращение к задачам, эпикам и подзадачам");
        System.out.println("Задачи:");
        for (Task task: manager.getTasks()) {
            System.out.println(manager.getTask(task.getId()));
        }
        System.out.println("Эпик:" + manager.getEpic(epicId3));
        System.out.println("Подзадача: " + manager.getSubTask(subTaskId11));
        System.out.println("Повторное обращение к 3 задаче" + manager.getTask(taskId3));
        System.out.println("Повторное обращение к 3 задаче" + manager.getTask(taskId3));

        System.out.println("\n***\n    Этап 3. История обращения к задачам:");
        for (Task task : manager.getHistoryManager()) {
            System.out.println(task);
        }

        System.out.println("\n***\n    Этап 4. Проверки истории задач, после из удаления.");
        manager.removeTask(taskId3);
        System.out.println("\n  После удаления 3-ей задачи история обращений следующая.");
        for (Task task : manager.getHistoryManager()) {
            System.out.println(task);
        }
        manager.clearTask();
        System.out.println("\n  После удаления всех задач история обращений следующая.");
        for (Task task : manager.getHistoryManager()) {
            System.out.println(task);
        }
    }
}
