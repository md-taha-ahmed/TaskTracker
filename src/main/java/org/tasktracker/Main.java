package org.tasktracker;

import org.tasktracker.models.Status;
import org.tasktracker.models.Task;
import org.tasktracker.services.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: task-cli [add|update|delete|list] [parameters]");
            return;
        }
        String command = args[0];

        switch (command) {
            case "add":
                if (args.length < 2) {
                    System.out.println("Usage: task-cli add <description>");
                    return;
                }
                int addId = TaskService.getLastId() + 1;
                String addDescription = args[1];
                Boolean addResult = TaskService.saveTask(new Task(addId, addDescription, Status.PENDING));
                if (addResult) {
                    System.out.println("Task added successfully.");
                } else {
                    System.out.println("Something went wrong.");
                }

            case "update":
                if (args.length < 3) {
                    System.out.println("Usage: task-cli update <id> <new description>");
                    return;
                }

                int updateId = Integer.parseInt(args[1]);
                String updateDescription = args[2];

                Boolean updateResult = TaskService.updateTaskById(updateId, updateDescription);
                if (updateResult) {
                    System.out.println("Task updated successfully.");
                } else {
                    System.out.println("Something went wrong.");
                }

            case "delete":
                if (args.length < 2) {
                    System.out.println("Usage: task-cli delete <id>");
                    return;
                }

                int deleteId = Integer.parseInt(args[1]);

                Boolean deleteResult = TaskService.deleteTask(deleteId);
                if (deleteResult) {
                    System.out.println("Task deleted successfully.");
                } else {
                    System.out.println("Something went wrong.");
                }

            case "list":
                List<Task> tasks = new ArrayList<>();
                if (args.length < 2) {
                    tasks = TaskService.loadTasks("all");

                } else {
                    if (Objects.equals(args[2], "done")) {
                        tasks = TaskService.loadTasks("done");
                    }
                    if (Objects.equals(args[2], "in-progress")) {
                        tasks = TaskService.loadTasks("in-progress");
                    }
                    if (Objects.equals(args[2], "todo")) {
                        tasks = TaskService.loadTasks("todo");
                    }
                }
                assert tasks != null;
                if (tasks.isEmpty()) {
                    System.out.println("No tasks were found.");
                } else {
                    for (Task task : tasks) {
                        System.out.println("---------------------------------------------");
                        System.out.println("ID: " + task.getId());
                        System.out.println("Description: " + task.getDescription());
                        System.out.println("Creation time: " + task.getCreatedAt());
                        System.out.println("Last update time: " + task.getUpdatedAt());
                        System.out.println("---------------------------------------------");
                    }
                }
            case "mark-in-progress":
                if (args.length < 2) {
                    System.out.println("Usage: task-cli mark-in-progress <id>");
                    return;
                }
                int progressId = Integer.parseInt(args[2]);
                TaskService.updateStatus(progressId, Status.IN_PROGRESS);
            case "mark-done":
                if (args.length < 2) {
                    System.out.println("Usage: task-cli mark-done <id>");
                    return;
                }
                int doneId = Integer.parseInt(args[2]);
                TaskService.updateStatus(doneId, Status.COMPLETED);
            case "mark-todo":
                if (args.length < 2) {
                    System.out.println("Usage: task-cli mark-todo <id>");
                    return;
                }
                int todoId = Integer.parseInt(args[2]);
                TaskService.updateStatus(todoId, Status.PENDING);
            default:
                System.out.println("Invalid command.");
        }
    }
}