package management;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private TaskManager taskManager;
    private Gson json;

    public HistoryHandler(TaskManager taskManager, Gson json) {
        this.taskManager = taskManager;
        this.json = json;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            List<Task> history = taskManager.getHistoryManager();
            String historyJson = json.toJson(history);
            sendText(exchange, historyJson, 200);
        }
    }
}
