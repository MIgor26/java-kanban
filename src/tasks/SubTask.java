package tasks;

public class SubTask extends Task {
    private int epicId; // Идентификатор эпика, к которому принадлежит подзадача

    // Конструктор для новой подзадачи
    public SubTask(String taskName, String taskDescription, Status status, int epicId) {
        super(taskName, taskDescription, status);
        this.epicId = epicId;
    }

    // Конструктор без изменения статуса
    public SubTask(String taskName, String taskDescription) {
        super(taskName, taskDescription);
    }

    // Конструктор новый для тестов
    public SubTask(String taskName, String taskDescription, Status status, int id, int epicId) {
        super(taskName, taskDescription, status, id);
        this.epicId = epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUB_TASK;
    }

    // Доступ к id эпика для подзадачи
    public int getEpicId() {
        return epicId;
    }

    // Установка id эпика для подзадачи
    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    // Вывод на печать наименование эпика и список подзадач для него с кол-вом символов описания
    @Override
    public String toString() {
        String result = "\"" + super.getTaskName() + "\", ";
        if (super.getTaskDescription() != null) {
            result = result + "описание: " + super.getTaskDescription().length() + " симв., ";
        } else {
            result = result + "описание отсутствует, ";
        }
        result = result + "для эпика с id = " + epicId;
        result = result + ", статус = " + super.getStatus();
        result = result + ", id = " + super.getId();
        return result;
    }
}
