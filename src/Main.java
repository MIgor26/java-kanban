//import management.FileBackedTaskManager;
//import management.Managers;
//import tasks.Epic;
//import tasks.Status;
//import tasks.SubTask;
//import tasks.Task;
//
//import java.io.File;
//import java.io.IOException;
//import java.time.Duration;
//import java.time.LocalDateTime;
//
//public class Main {
//    public static void main(String[] args) {
//        // Тесты Файлового Менеджера
//        File file1 = null;
//        try {
//            file1 = File.createTempFile("file", ".scv");
//        } catch (IOException e) {
//            System.out.println(e);
//        }
//        // Создаём переменные времени и продолжительности
//        LocalDateTime startTime = LocalDateTime.now();
//        LocalDateTime startTimeTask1 = startTime.plusHours(1);
//        LocalDateTime startTimeTask2 = startTime.plusHours(2);
//        LocalDateTime startTimeTask3 = startTime.plusHours(2);
//        LocalDateTime startTimeSubTask1 = startTime.plusHours(3);
//        LocalDateTime startTimeSubTask2 = startTime.plusHours(4);
//        LocalDateTime startTimeSubTask3 = startTime.plusHours(5);
//        Duration durationTask1 = Duration.ofMinutes(60);
//        Duration durationTask2 = Duration.ofMinutes(45);
//        Duration durationTask3 = Duration.ofMinutes(10);
//        Duration durationSubTask1 = Duration.ofMinutes(15);
//        Duration durationSubTask2 = Duration.ofMinutes(120);
//        Duration durationSubTask3 = Duration.ofMinutes(30);
//        // Создаём задачи
//        FileBackedTaskManager fbm = Managers.getDefaultFile(file1);
//        Task task1 = new Task("Task #1", "Task #1 description", Status.NEW, startTimeTask1, durationTask1);
//        Task task2 = new Task("Task #2", "Task #2 description", Status.NEW, startTimeTask2, durationTask2);
//        Task task3 = new Task("Task #3", "Task #3 description", Status.NEW, startTimeTask3, durationTask3);
//        final int taskId1 = fbm.addTask(task1);
//        final int taskId2 = fbm.addTask(task2);
//        final int taskId3 = fbm.addTask(task3);
//        Epic epic1 = new Epic("Epic #1", "Epic #1 description");
//        Epic epic2 = new Epic("Epic #2", "Epic #2 description");
//        final int epicId1 = fbm.addEpic(epic1);
//        final int epicId2 = fbm.addEpic(epic2);
//        SubTask subTask11 = new SubTask("SubTask #11", "", Status.NEW, epicId1, startTimeSubTask1, durationSubTask1);
//        SubTask subTask12 = new SubTask("SubTask #12", "", Status.DONE, epicId1, startTimeSubTask2, durationSubTask2);
//        SubTask subTask13 = new SubTask("SubTask #13", "", Status.IN_PROGRESS, epicId1, startTimeSubTask3, durationSubTask3);
//        final int subTaskId11 = fbm.addSubTask(subTask11);
//        final int subTaskId12 = fbm.addSubTask(subTask12);
//        final int subTaskId13 = fbm.addSubTask(subTask13);
//        SubTask subTask21 = new SubTask("SubTask #21", "", Status.DONE, epicId2, startTimeSubTask1.plusHours(24), durationSubTask1);
//        final int subTaskId21 = fbm.addSubTask(subTask21);
//        // Проверка расчёта времён эпика
//        System.out.println("Начало эпика 1 = " + fbm.getEpic(epicId1).getStartTime());
//        System.out.println("Продолжительность эпика 1 = " + fbm.getEpic(epicId1).getDuration());
//        System.out.println("Окончание эпика 1 = " + fbm.getEpic(epicId1).getEndTime());
//        System.out.println("***");
//        System.out.println("Начало эпика 2 = " + fbm.getEpic(epicId2).getStartTime());
//        System.out.println("Продолжительность эпика 2 = " + fbm.getEpic(epicId2).getDuration());
//        System.out.println("Окончание эпика 2 = " + fbm.getEpic(epicId2).getEndTime());
//        System.out.println("\n***\n");
//        // Вывод на экран задач
//        System.out.println("Список задач из менеджера по умолчанию");
//        System.out.println("Список задач: " + fbm.getTasks());
//        System.out.println("Список эпиков: " + fbm.getEpics());
//        System.out.println("Список подзадач: " + fbm.getSubTasks());
//        System.out.println("\n Список по приоритетам: ");
//        for (Task task : fbm.getPrioritizedTasks()) {
//            System.out.println(task);
//        }
//        System.out.println("\n***\n");
//        // Загружаем менеджер из файла
//        FileBackedTaskManager fbm1 = FileBackedTaskManager.loadFromFile(file1);
//        // Вывод на экран задач
//        System.out.println("Список задач из менеджера, восстановленного из файла");
//        System.out.println("Список задач: " + fbm1.getTasks());
//        System.out.println("Список эпиков: " + fbm1.getEpics());
//        System.out.println("Список подзадач: " + fbm1.getSubTasks());
//        // Удаление задачи 1 и эпика 1
//        fbm1.removeTask(taskId1);
//        fbm1.removeEpic(epicId1);
//        System.out.println("\n Список по приоритетам после удаления 1 задачи и 1 эпика: ");
//        for (Task task : fbm1.getPrioritizedTasks()) {
//            System.out.println(task);
//        }
//        System.out.println("\n***\n");
//        // Удаляем все подзадачи
//        fbm1.clearSubTask();
//        System.out.println("Список эпиков после удаления подзадач");
//        System.out.println("Список эпиков: " + fbm1.getEpics());
//        System.out.println("Список по приоритетам после удаления всех подзадач");
//        for (Task task : fbm1.getPrioritizedTasks()) {
//            System.out.println(task);
//        }
//        // Загрузка вторая
//        System.out.println("Список эпиков после удаления подзадач из загруженного файла");
//        FileBackedTaskManager fbm2 = FileBackedTaskManager.loadFromFile(file1);
//        System.out.println("Список эпиков: " + fbm2.getEpics());
//
//
//    }
//}
