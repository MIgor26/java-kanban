package management;

import adapters.DurationTimeAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.ErrorResponse;
import exception.ManagerSaveException;
import exception.TaskNotFoundException;
import exception.ValidateException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpHandler implements HttpHandler {

    LocalDateTimeAdapter localDateTimeAdapter = new LocalDateTimeAdapter();
    DurationTimeAdapter durationTimeAdapter = new DurationTimeAdapter();
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson json;

    public BaseHttpHandler() {
        gsonBuilder.setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, localDateTimeAdapter)
                .registerTypeAdapter(Duration.class, durationTimeAdapter);
        json = gsonBuilder.create();
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
        } catch (
                Exception e) { // Данный блок не по ТЗ. Код ошибки придуман. Для отладки кода и отлова прочих исключений
            ErrorResponse response = new ErrorResponse(e.getMessage(), 501, exchange.getRequestURI().getPath());
            String jsonText = json.toJson(response);
            sendText(exchange, jsonText, 501);
        } finally {
            exchange.close();
        }
    }

    void handleGet(HttpExchange exchange) throws IOException {
    }

    void handleDelete(HttpExchange exchange) throws IOException {
    }

    void handlePost(HttpExchange exchange) throws IOException {
    }

    protected void sendText(HttpExchange h, String text, int code) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }
}