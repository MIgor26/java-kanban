package management;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import tasks.Epic;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler {
    private TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
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
            Epic task = taskManager.getEpic(taskId);
            String taskJson = json.toJson(task);
            sendText(exchange, taskJson, 200);
        }
        // Если длина пути равна 2, то выводим все задачи
        if (urlPath.length == 2) {
            List<Epic> allTask = taskManager.getEpics();
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
        taskManager.removeEpic(taskId);
        sendText(exchange, "Эпик по id " + taskId + " удален.", 200);
    }

    @Override
    void handlePost(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        // Обязательные поля сохраняем в переменные, не проверяя
        String taskName = jsonObject.get("taskName").getAsString();
        String taskDescription = jsonObject.get("taskDescription").getAsString();
        // Если id присутствует - считаем, что это обновление задачи
        if (jsonObject.has("id")) {
            int id = jsonObject.get("id").getAsInt();
            Epic task = new Epic(taskName, taskDescription, id);
            taskManager.updateEpic(task);
            sendText(exchange, "Эпик успешно обновлен.", 201);

        } else {
            Epic task = new Epic(taskName, taskDescription);
            taskManager.addEpic(task);
            sendText(exchange, "Эпик успешно создан.", 201);
        }
    }
}
