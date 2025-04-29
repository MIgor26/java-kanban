package management;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpServer;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HttpTaskServer {

    public static void main(String[] args) throws IOException {
        int PORT = 8080;
        TaskManager taskManager = Managers.getDefault();
        // Создание json
        LocalDateTimeAdapter localDateTimeAdapter = new LocalDateTimeAdapter();
        DurationTimeAdapter durationTimeAdapter = new DurationTimeAdapter();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, localDateTimeAdapter)
                .registerTypeAdapter(Duration.class, durationTimeAdapter);
        Gson json = gsonBuilder.create();
        //Создание задач
        LocalDateTime start1 = LocalDateTime.now();
        LocalDateTime start2 = start1.plusHours(1);
        LocalDateTime start3 = start2.plusHours(1);
        LocalDateTime start4 = start3.plusHours(1);
        Duration duration = Duration.ofMinutes(30);
        taskManager.addTask(new Task("Task", "1", Status.NEW, start1, duration));
        int idEp1 = taskManager.addEpic(new Epic("Epic1", "1"));
        int idEp2 = taskManager.addEpic(new Epic("Epic2", "1"));
        taskManager.addSubTask(new SubTask("SubTask", "11", Status.NEW, idEp1, start2, duration));
        taskManager.addSubTask(new SubTask("SubTask", "12", Status.IN_PROGRESS, idEp1, start3, duration));
        taskManager.addSubTask(new SubTask("SubTask", "21", Status.DONE, idEp2, start4, duration));
        // Создание сервера
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager, json));
        httpServer.createContext("/subtasks", new SubTaskHandler(taskManager, json));
        httpServer.createContext("/epics", new EpicHandler(taskManager, json));
        httpServer.createContext("/history", new HistoryHandler(taskManager, json));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager, json));
        httpServer.start(); // !! Сделать метод на Старт
        System.out.println("Сервер запущен на " + PORT + " порту.");
    }
}

class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDate) throws IOException {
        jsonWriter.value(localDate.format(dtf));
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), dtf);
    }
}

class DurationTimeAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
        jsonWriter.value(duration.toMinutes());
    }

    @Override
    public Duration read(final JsonReader jsonReader) throws IOException {
        return Duration.parse(jsonReader.nextString());
    }
}