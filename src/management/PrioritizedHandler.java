package management;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private TaskManager taskManager;
    private Gson json;

    public PrioritizedHandler(TaskManager taskManager, Gson json) {
        this.taskManager = taskManager;
        this.json = json;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            List<Task> prioritized = taskManager.getPrioritizedTasks();
            String prioritizedJson = json.toJson(prioritized);
            sendText(exchange, prioritizedJson, 200);
        }
    }
}
