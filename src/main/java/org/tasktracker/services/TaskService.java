package org.tasktracker.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.tasktracker.models.Status;
import org.tasktracker.models.Task;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TaskService {
    private static final String FILE_PATH = System.getProperty("user.home") + "/task-tracker/tasks.json";
    private static final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();


    // Save a task to JSON
    public static Boolean saveTask(Task task) {
        try {
            File file = new File(FILE_PATH);
            // Ensure the folder exists
            file.getParentFile().mkdirs();

            List<Task> tasks;
            if (file.exists() && file.length() > 0) { // Check if file exists AND is not empty
                try {
                    tasks = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Task[].class)));
                } catch (IOException e) {
                    e.printStackTrace();
                    tasks = new ArrayList<>(); // Fallback to an empty list if JSON is invalid
                }
            } else {
                tasks = new ArrayList<>(); // If file does not exist OR is empty, create an empty list
            }

            tasks.add(task); // Add the new task

            objectMapper.writeValue(file, tasks); // Save updated tasks list
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //load Tasks
    public static List<Task> loadTasks(String taskStatus) {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return null;
            List<Task> tasks = new ArrayList<>(List.of(objectMapper.readValue(file, Task[].class)));
            List<Task> resultTasks = new ArrayList<>();
            if (Objects.equals(taskStatus, "done")) {
                for (Task task : tasks) {
                    if (task.getStatus() == Status.COMPLETED) {
                        resultTasks.add(task);
                    }
                }
                return resultTasks;
            } else if (Objects.equals(taskStatus, "todo")) {

                for (Task task : tasks) {
                    if (task.getStatus() == Status.PENDING) {
                        resultTasks.add(task);
                    }
                    return resultTasks;
                }
            } else if (Objects.equals(taskStatus, "in-progress")) {
                for (Task task : tasks) {
                    if (task.getStatus() == Status.IN_PROGRESS) {
                        resultTasks.add(task);
                    }
                }
                return resultTasks;
            }
            return tasks;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Load a task by ID
    public static Task loadTaskById(int id) {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return null;

            List<Task> tasks = new ArrayList<>(List.of(objectMapper.readValue(file, Task[].class)));

            for (Task task : tasks) {
                if (task.getId() == id) return task;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static Boolean updateTaskById(int id, String newTask) {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return false;
            List<Task> tasks = new ArrayList<>(List.of(objectMapper.readValue(file, Task[].class)));
            for (Task task : tasks) {
                if (task.getId() == id) {
                    task.setDescription(newTask);
                    task.setUpdatedAt(LocalDateTime.now());
                }
            }
            objectMapper.writeValue(file, tasks);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

    //  update status of a task
    public static Boolean updateStatus(int id, Status status) {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return false;
            List<Task> tasks = new ArrayList<>(List.of(objectMapper.readValue(file, Task[].class)));
            for (Task task : tasks) {
                if (task.getId() == id) {
                    task.setStatus(status);
                }
            }
            objectMapper.writeValue(file, tasks);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean deleteTask(int id) {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return false;
            List<Task> tasks = new ArrayList<>(List.of(objectMapper.readValue(file, Task[].class)));
            tasks.removeIf(task -> task.getId() == id);
            objectMapper.writeValue(file, tasks);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getLastId() {
        List<Task> tasks = loadTasks("all");
        assert tasks != null;
        return tasks.isEmpty() ? 1 : tasks.getLast().getId();
    }
}
