package management;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.ErrorResponse;
import exception.ManagerSaveException;
import exception.TaskNotFoundException;
import exception.ValidateException;
import tasks.Status;
import tasks.SubTask;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {
    private TaskManager taskManager;
    private Gson json;

    public SubTaskHandler(TaskManager taskManager, Gson json) {
        this.taskManager = taskManager;
        this.json = json;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        try {
            switch (method) {
                case "GET":
                    handleGet(exchange);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange);
                    break;
                default:
                    break;
            }
        } catch (TaskNotFoundException e) {
            ErrorResponse response = new ErrorResponse(e.getMessage(), 404, exchange.getRequestURI().getPath());
            String jsonText = json.toJson(response);
            sendText(exchange, jsonText, 404);
        } catch (ValidateException e) {
            ErrorResponse response = new ErrorResponse(e.getMessage(), 406, exchange.getRequestURI().getPath());
            String jsonText = json.toJson(response);
            sendText(exchange, jsonText, 406);
        } catch (ManagerSaveException e) {
            ErrorResponse response = new ErrorResponse(e.getMessage(), 500, exchange.getRequestURI().getPath());
            String jsonText = json.toJson(response);
            sendText(exchange, jsonText, 500);
        } finally {
            exchange.close();
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        URI request = exchange.getRequestURI();
        String path = request.getPath();
        String[] urlPath = path.split("/");
        // Если длина пути равна 3, значит присутствует id и вывозим задачу по id
        if (urlPath.length == 3) {
            int taskId = Integer.parseInt(urlPath[2]);
            SubTask task = taskManager.getSubTask(taskId);
            String taskJson = json.toJson(task);
            sendText(exchange, taskJson, 200);
        }
        // Если длина пути равна 2, то выводим все задачи
        if (urlPath.length == 2) {
            List<SubTask> allTask = taskManager.getSubTasks();
            String allTaskJson = json.toJson(allTask);
            sendText(exchange, allTaskJson, 200);
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        URI request = exchange.getRequestURI();
        String path = request.getPath();
        String[] urlPath = path.split("/");
        int taskId = Integer.parseInt(urlPath[2]);
        taskManager.removeSubTask(taskId);
        sendText(exchange, "Подзадача по id " + taskId + " удалена.", 200);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        // Обязательные поля сохраняем в переменные, не проверяя
        String taskName = jsonObject.get("taskName").getAsString();
        String taskDescription = jsonObject.get("taskDescription").getAsString();
        String status = jsonObject.get("status").getAsString();
        int epicId = jsonObject.get("epicId").getAsInt();
        // Временные поля могут быть не заданы - проверяем это
        String startTimeStr = jsonObject.get("startTime").getAsString();
        LocalDateTime startTime = null;
        if (jsonObject.has("startTime")) {
            startTime = LocalDateTime.parse(startTimeStr, dtf);
        }
        int durationMin = jsonObject.get("duration").getAsInt();
        Duration duration = Duration.ZERO;
        if (jsonObject.has("duration")) {
            duration = Duration.ofMinutes(durationMin);
        }
        // Если id присутствует - считаем, что это обновление задачи
        if (jsonObject.has("id")) {
            int id = jsonObject.get("id").getAsInt();
            System.out.println("Формируем задачу");
            SubTask task = new SubTask(taskName, taskDescription, Status.valueOf(status), startTime, duration, id, epicId);
            System.out.println(task);
            taskManager.updateSubTask(task);
            sendText(exchange, "Подзадача успешно обновлена.", 201);

        } else {
            SubTask task = new SubTask(taskName, taskDescription, Status.valueOf(status), epicId, startTime, duration);
            taskManager.addSubTask(task);
            sendText(exchange, "Подзадача успешно создана.", 201);
        }
    }
}