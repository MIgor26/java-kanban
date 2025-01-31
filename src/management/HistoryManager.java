package management;

import tasks.Task;

import java.util.List;

public interface HistoryManager {

    void getHistory(Task task);

    List<Task> getHistory();

}