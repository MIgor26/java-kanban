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
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private TaskManager taskManager;
    private Gson json;

    public EpicHandler(TaskManager taskManager, Gson json) {
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
            Epic task = taskManager.getEpic(taskId);
            System.out.println(task);
            // !!! Начало Костыля. Но он не работает.
            if (task.getSubTaskIds().isEmpty()) {
                task.putEpicSubTasks(new SubTask("", "", Status.NEW, task.getId()));
            }
            // !!! Окончание костыля
            String taskJson = json.toJson(task); // ?? Не пойму, почему не сериализуется эпик, у которого нет подзадачи
            sendText(exchange, taskJson, 200);
        }
        // Если длина пути равна 2, то выводим все задачи
        if (urlPath.length == 2) {
            List<Epic> allTask = taskManager.getEpics();
            String allTaskJson = json.toJson(allTask);
            sendText(exchange, allTaskJson, 200);
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        URI request = exchange.getRequestURI();
        String path = request.getPath();
        String[] urlPath = path.split("/");
        int taskId = Integer.parseInt(urlPath[2]);
        taskManager.removeEpic(taskId);
        sendText(exchange, "Эпик по id " + taskId + " удален.", 200);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
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
