package management;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> viewed10Tasks = new ArrayList<>();

    @Override
    public void getHistory(Task task) {
        if (viewed10Tasks.size() > 9) {
            viewed10Tasks.remove(0);
        }
        viewed10Tasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> copyVieved10Tasks = new ArrayList<>(viewed10Tasks);
        return copyVieved10Tasks;
    }
}