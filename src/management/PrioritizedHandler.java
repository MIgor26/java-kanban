package management;

import com.sun.net.httpserver.HttpExchange;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler {
    private TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        super();
        this.taskManager = taskManager;
    }

    @Override
    void handleGet(HttpExchange exchange) throws IOException {
        List<Task> prioritized = taskManager.getPrioritizedTasks();
        String prioritizedJson = json.toJson(prioritized);
        sendText(exchange, prioritizedJson, 200);

    }
}
