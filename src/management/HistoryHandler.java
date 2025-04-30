package management;

import com.sun.net.httpserver.HttpExchange;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    private TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        super();
        this.taskManager = taskManager;
    }

    void handleGet(HttpExchange exchange) throws IOException {
        List<Task> history = taskManager.getHistoryManager();
        String historyJson = json.toJson(history);
        sendText(exchange, historyJson, 200);

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
