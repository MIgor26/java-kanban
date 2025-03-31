package management;

import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
        HistoryManager historyManager = Managers.getDefaultHistory();
        manager.loadFromFile(); // Отдельный нестатический метод, так как поля нестатические
        return manager;
    }

    // Формирование полей Файл Менеджера
    private void loadFromFile() {
        // Files.readString(file.toPath()); !! Так было предложено в ТЗ. Я не знаю как лучше
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
        } catch (FileNotFoundException e) { // ?? Нужно ли своё исключение добавить
            e.printStackTrace();
        } catch (IOException e) { // ?? Нужно ли своё исключение добавить
            e.printStackTrace();
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
                    + task.getTaskDescription() + "," + ((SubTask)task).getEpicId());
            return taskStr;
        } else {
            String taskStr = (task.getId() + "," + type + "," + task.getTaskName() + "," + task.getStatus() + ","
                    + task.getTaskDescription() + ",");
            return taskStr;
        }
    }

    // Получение списка задач (a)
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    // Получение списка эпиков (a)
    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    // Получение списка подзадач (a)
    @Override
    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    // Удаление всех задач (b)
    @Override
    public void clearTask() {
        for (Task task : tasks.values()) { // Удаление всех задач из истории просмотра
            historyManager.remove(task.getId());
        }
        tasks.clear();
        save();
    }

    // Удаление всех эпиков (b)
    @Override
    public void clearEpic() {
        for (Epic epic : epics.values()) { // Удаление всех эпиков из истории просмотра
            historyManager.remove(epic.getId());
        }
        epics.clear();
        for (SubTask subTask : subTasks.values()) { // Удаление всех подзадач из истории просмотра
            historyManager.remove(subTask.getId());
        }
        subTasks.clear(); // Также удаляются все подзадачи
        save();
    }

    // Удаление всех подзадач (b)
    @Override
    public void clearSubTask() {
        for (SubTask subTask : subTasks.values()) { // Удаление всех подзадач из истории просмотра
            historyManager.remove(subTask.getId());
        }
        subTasks.clear();
        for (Integer id : epics.keySet()) {
            epics.get(id).setStatus(Status.NEW);// Присваивается статус NEW всем эпикам
        }
        for (Epic epic : epics.values()) { // Удаление для всех эпиков мап с подзадачами
            epic.clearEpicSubTask();
        }
        save();
    }

    // Получение по id задачи (c)
    @Override
    public Task getTask(int id) {
        if (tasks.get(id) != null) {
            historyManager.add(tasks.get(id));
        }
        return tasks.get(id);
    }

    // Получение по id эпика (c)
    @Override
    public Epic getEpic(int id) {
        if (epics.get(id) != null) {
            historyManager.add(epics.get(id));
        }
        return epics.get(id);
    }

    // Получение по id подзадачи (c)
    @Override
    public SubTask getSubTask(int id) {
        if (subTasks.get(id) != null) {
            historyManager.add(subTasks.get(id));
        }
        return subTasks.get(id);
    }

    // Создание новой задачи (d)
    @Override
    public int addTask(Task newTask) {
        countId++;
        newTask.setId(countId);
        tasks.put(countId, newTask);
        save();
        return countId;
    }

    // Создание нового эпика (d)
    @Override
    public int addEpic(Epic newEpic) {
        countId++;
        newEpic.setId(countId);
        epics.put(countId, newEpic);
        save();
        return countId;
    }

    // Создание новой подзадачи (d)
    @Override
    public int addSubTask(SubTask newSubTask) {
        countId++;
        newSubTask.setId(countId);
        subTasks.put(countId, newSubTask);
        epics.get(newSubTask.getEpicId()).putEpicSubTasks(newSubTask); // Добавление подзадачи в список эпика
        updateEpicStatus(newSubTask.getEpicId()); // Обновление статуса эпика к которому относится подзадача
        save();
        return countId;
    }

    // Обновление задачи (e)
    @Override
    public void updateTask(Task updTask) {
        tasks.put(updTask.getId(), updTask);
        save();
    }

    // Обновление эпика (e)
    @Override
    public void updateEpic(Epic updEpic) {
        epics.put(updEpic.getId(), updEpic);
        save();
    }

    // Обновление подзадачи (e)
    @Override
    public void updateSubTask(SubTask updSubTask) {
        subTasks.put(updSubTask.getId(), updSubTask);
        updateEpicStatus(updSubTask.getEpicId()); // Обновление статуса эпика к которому относится подзадача
        save();
    }

    // Удаление задачи по id (f)
    @Override
    public void removeTask(int id) {
        historyManager.remove(id);
        tasks.remove(id);
        save();
    }

    // Удаление эпика по id (f)
    @Override
    public void removeEpic(int id) {
        historyManager.remove(id);
        final Epic epic = epics.remove(id); // ??Не помню, почему final
        for (Integer subtaskId : epic.getSubTaskIds()) {
            historyManager.remove(subtaskId); // Удаление Субтасков из истории при удалении Эпика
            subTasks.remove(subtaskId);
            save();
        }
    }

    // Удаление подзадачи по id (f)
    @Override
    public void removeSubTask(int id) {
        historyManager.remove(id);
        int epicId = subTasks.get(id).getEpicId(); // Получение id эпика, т. к. после удаления подзадачи его не получишь
        subTasks.remove(id);
        epics.get(epicId).removeEpicSubTask(id); // Удаление подзадачи в поле эпика
        updateEpicStatus(epicId); // Обновление статуса эпика к которому относится подзадача
        save();
    }

    // Получение подзадач для заданного по id эпика
    @Override
    public ArrayList<SubTask> getEpicSubTasks(int id) { // на вход поступает id эпика
        if (epics.get(id) == null) {
            return new ArrayList<SubTask>();
        }
        return epics.get(id).getEpicSubTasks(); // Возвращаем подзадачи для эпика
    }

    // Обновление статуса эпика
    @Override
    public void updateEpicStatus(int epicId) {
        if (getEpicSubTasks(epicId).isEmpty()) { // Проверка на наличие подзадач (рефакторинг)
            epics.get(epicId).setStatus(Status.NEW); // Если подзадач нет, то статус эпика = NEW
            return;
        }
        ArrayList<SubTask> tasksForEpic = getEpicSubTasks(epicId); // Получение всех подзадач для эпика
        int counterDone = 0; // Счётчик статусов подзадач со статусом DONE (не написал count, т.к. смешно читается :)
        int counterNew = 0; // Счётчик статусов подзадач со статусом NEW
        for (SubTask task : tasksForEpic) { // Перебор id подзадач эпика
            if (task.getStatus() == Status.DONE) { // Проверка является ли статус подзадачи DONE
                counterDone++;
            } else if (task.getStatus() == Status.NEW) {
                counterNew++;
            }
        }
        if (counterDone == tasksForEpic.size()) { // Если все подзадачи имеют статус DONE, то статус эпика = DONE
            epics.get(epicId).setStatus(Status.DONE);
        } else if (counterNew == tasksForEpic.size()) { // Если все подзадачи имеют статус NEW, то статус эпика = NEW
            epics.get(epicId).setStatus(Status.NEW);
        } else { // Во всех остальных случаях статус эпика = IN PROGRESS
            epics.get(epicId).setStatus(Status.IN_PROGRESS);
        }
    }
}



