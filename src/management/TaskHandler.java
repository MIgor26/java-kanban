package management;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import tasks.Status;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskHandler extends BaseHttpHandler {
    private TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        super();
        this.taskManager = taskManager;
    }

    @Override
    void handleGet(HttpExchange exchange) throws IOException {
        URI request = exchange.getRequestURI();
        String path = request.getPath();
        String[] urlPath = path.split("/");
        // Если длина пути равна 3, значит присутствует id и вывозим задачу по id
        if (urlPath.length == 3) {
            int taskId = Integer.parseInt(urlPath[2]);
            Task task = taskManager.getTask(taskId);
            String taskJson = json.toJson(task);
            sendText(exchange, taskJson, 200);
        }
        // Если длина пути равна 2, то выводим все задачи
        if (urlPath.length == 2) {
            List<Task> allTask = taskManager.getTasks();
            String allTaskJson = json.toJson(allTask);
            sendText(exchange, allTaskJson, 200);
        }
    }

    @Override
    void handleDelete(HttpExchange exchange) throws IOException {
        URI request = exchange.getRequestURI();
        String path = request.getPath();
        String[] urlPath = path.split("/");
        int taskId = Integer.parseInt(urlPath[2]);
        taskManager.removeTask(taskId);
        sendText(exchange, "Задача по id " + taskId + " удалена.", 200);
    }

    @Override
    void handlePost(HttpExchange exchange) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        // Обязательные поля сохраняем в переменные, не проверяя
        String taskName = jsonObject.get("taskName").getAsString();
        String taskDescription = jsonObject.get("taskDescription").getAsString();
        String status = jsonObject.get("status").getAsString();
        System.out.println("status = " + status);
        // Временные поля могут быть не заданы - проверяем это
        LocalDateTime startTime = null;
        if (jsonObject.has("startTime")) {
            startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString(), dtf);
        }
        Duration duration = Duration.ZERO;
        if (jsonObject.has("duration")) {
            duration = Duration.ofMinutes(jsonObject.get("duration").getAsInt());
        }
        // Если id присутствует - считаем, что это обновление задачи
        if (jsonObject.has("id")) {
            int id = jsonObject.get("id").getAsInt();
            Task task = new Task(taskName, taskDescription, Status.valueOf(status), startTime, duration, id);
            taskManager.updateTask(task);
            sendText(exchange, "Задача успешно обновлена.", 201);

        } else {
            Task task = new Task(taskName, taskDescription, Status.valueOf(status), startTime, duration);
            System.out.println("Task = " + task);
            taskManager.addTask(task);
            sendText(exchange, "Задача успешно создана.", 201);
        }
    }
}