import management.*;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        File file1 = null;
        try {
            file1 = File.createTempFile("file", ".scv");
        } catch (IOException e) {
            System.out.println(e);
        }
        FileBackedTaskManager fbm = Managers.getDefaultFile(file1);
        Task task1 = new Task("Task #1", "Task #1 description", Status.NEW);
        Task task2 = new Task("Task #2", "Task #2 description", Status.NEW);
        final int taskId1 = fbm.addTask(task1);
        final int taskId2 = fbm.addTask(task2);
        Epic epic1 = new Epic("Epic #1", "Epic #1 description");
        Epic epic2 = new Epic("Epic #2", "Epic #2 description");
        final int epicId1 = fbm.addEpic(epic1);
        final int epicId2 = fbm.addEpic(epic2);
        SubTask subTask11 = new SubTask("SubTask #11", "", Status.NEW, epicId1);
        final int subTaskId11 = fbm.addSubTask(subTask11);

        FileBackedTaskManager fbm1 = FileBackedTaskManager.loadFromFile(file1);

        System.out.println("Список задач из менеджера по умолчанию");
        System.out.println("Список задач: " + fbm.getTasks());
        System.out.println("Список эпиков: " + fbm.getEpics());
        System.out.println("Список подзадач: " + fbm.getSubTasks());
        System.out.println("***");
        System.out.println("Список задач из менеджера, восстановленного из файла");
        System.out.println("Список задач: " + fbm1.getTasks());
        System.out.println("Список эпиков: " + fbm1.getEpics());
        System.out.println("Список подзадач: " + fbm1.getSubTasks());
        

        File file2 = new File("D:\\Java\\Project\\java-kanban\\src\\resources\\tasks.csv");
        FileBackedTaskManager fbm2 = FileBackedTaskManager.loadFromFile(file2);
        System.out.println("***");
        System.out.println("Список задач из стороннего файла");
        System.out.println("Список задач: " + fbm2.getTasks());
        System.out.println("Список эпиков: " + fbm2.getEpics());
        System.out.println("Список подзадач: " + fbm2.getSubTasks());
        System.out.println("Получение по ИД Задачи = " + fbm2.getTask(1));

        fbm2.updateTask(new Task("Task1", "hj", Status.IN_PROGRESS, 1));
        System.out.println("Обновлённая задача 1 = " + fbm2.getTask(1));
        ArrayList<SubTask> listForEpic = fbm2.getEpicSubTasks(2);
        System.out.println("Подзадачи для Эпика " + listForEpic);



    }
}
