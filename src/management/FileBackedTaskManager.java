package management;

import exception.ManagerSaveException;
import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    // Загрузка из файла Таск Менеджера
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        HistoryManager historyManager = Managers.getDefaultHistory(); // МБ передать в конструктор
        manager.loadFromFile(); // Отдельный нестатический метод, так как поля нестатические
        return manager;
    }

    // Формирование полей Файл Менеджера
    private void loadFromFile() {
        try (FileReader fr = new FileReader(file, StandardCharsets.UTF_8); BufferedReader br = new BufferedReader(fr)) {
            br.readLine(); // Пропускаем первую строку
            int maxId = 0;

            while (br.ready()) {
                String line = br.readLine();
                Task task = fromString(line);
                final int id = task.getId();
                if (id > maxId) maxId = id;
                if (task.getType() == TaskType.EPIC) {
                    epics.put(id, (Epic) task);
                } else if (task.getType() == TaskType.TASK) {
                    tasks.put(id, task);
                } else {
                    subTasks.put(id, (SubTask) task);
                    int epicId = ((SubTask) task).getEpicId();
                    epics.get(epicId).putEpicSubTasks((SubTask) task); // Кладём Сабтаск в список Эпика. Способ работает,
                    // в случае, если первыми в файле идут Эпики.
                }
            }
            countId = maxId;

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении из файла: " + e.getMessage(), e);
        }
    }

    // Формирование задачи из очередной считанной линии из файла
    private Task fromString(String value) {
        String[] taskStr = value.split(",", 6); // !! Проверить для чего лимит
        Task task = null;
        TaskType type = TaskType.valueOf(taskStr[1]);
        switch (type) {
            case EPIC:
                task = new Epic(taskStr[2], taskStr[4], Status.valueOf(taskStr[3]), Integer.parseInt(taskStr[0]));
                break;
            case TASK:
                task = new Task(taskStr[2], taskStr[4], Status.valueOf(taskStr[3]), Integer.parseInt(taskStr[0]));
                break;
            case SUB_TASK:
                task = new SubTask(taskStr[2], taskStr[4], Status.valueOf(taskStr[3]), Integer.parseInt(taskStr[0]),
                        Integer.parseInt(taskStr[5]));
                break;
        }
        return task;
    }

    // Сохранение Файл менеджера в файл
    private void save() {
        try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write("id,type,name,status,description,epic" + "\n"); // Запись заголовка
            // Сначала записываем эпики, для корректной работы метода loadFromFile, а именно эпики должны считываться
            // до сабтасков, чтобы в эпики добавить список сабтасков
            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                bw.append(toString(entry.getValue()));
                bw.newLine();
            }
            // Запись тасков
            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                bw.append(toString(entry.getValue()));
                bw.newLine();
            }
            // Запись сабтасков
            for (Map.Entry<Integer, SubTask> entry : subTasks.entrySet()) {
                bw.append(toString(entry.getValue()));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в файле: " + file.getName(), e);
        }
    }

    // Сохранение задачи в строку
    private String toString(Task task) {
        TaskType type = task.getType();
        if (type.equals(TaskType.SUB_TASK)) {
            String taskStr = (task.getId() + "," + type + "," + task.getTaskName() + "," + task.getStatus() + ","
                    + task.getTaskDescription() + "," + ((SubTask) task).getEpicId());
            return taskStr;
        } else {
            String taskStr = (task.getId() + "," + type + "," + task.getTaskName() + "," + task.getStatus() + ","
                    + task.getTaskDescription() + ",");
            return taskStr;
        }
    }

    // Удаление всех задач
    @Override
    public void clearTask() {
        super.clearTask();
        save();
    }

    // Удаление всех эпиков
    @Override
    public void clearEpic() {
        super.clearEpic();
        save();
    }

    // Удаление всех подзадач
    @Override
    public void clearSubTask() {
        super.clearSubTask();
        save();
    }

    // Создание новой задачи
    @Override
    public int addTask(Task newTask) {
        super.addTask(newTask);
        save();
        return countId;
    }

    // Создание нового эпика
    @Override
    public int addEpic(Epic newEpic) {
        super.addEpic(newEpic);
        save();
        return countId;
    }

    // Создание новой подзадачи
    @Override
    public int addSubTask(SubTask newSubTask) {
        super.addSubTask(newSubTask);
        save();
        return countId;
    }

    // Обновление задачи
    @Override
    public void updateTask(Task updTask) {
        super.updateTask(updTask);
        save();
    }

    // Обновление эпика
    @Override
    public void updateEpic(Epic updEpic) {
        super.updateEpic(updEpic);
        save();
    }

    // Обновление подзадачи
    @Override
    public void updateSubTask(SubTask updSubTask) {
        super.updateSubTask(updSubTask);
        save();
    }

    // Удаление задачи по id
    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    // Удаление эпика по id
    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    // Удаление подзадачи по id
    @Override
    public void removeSubTask(int id) {
        super.removeSubTask(id);
        save();
    }
}



