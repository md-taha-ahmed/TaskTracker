package org.tasktracker;

import org.tasktracker.models.Status;
import org.tasktracker.models.Task;
import org.tasktracker.services.TaskService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0 || !args[0].equals("task-cli")) {
            System.out.println("Usage: task-cli [add|update|delete|list|mark-in-progress|mark-done|mark-todo] [parameters]");
            return;
        }

        if (args.length < 2) {
            System.out.println("Error: No command provided.");
            return;
        }

        String command = args[1];

        switch (command) {
            case "add":
                if (args.length < 3) {
                    System.out.println("Usage: task-cli add <description>");
                    return;
                }
                int addId = TaskService.getLastId() + 1;
                String addDescription = args[2];
                boolean addResult = TaskService.saveTask(new Task(addId, addDescription, Status.PENDING));

                System.out.println(addResult ? "Task added successfully." : "Something went wrong.");
                break;

            case "update":
                if (args.length < 4) {
                    System.out.println("Usage: task-cli update <id> <new description>");
                    return;
                }
                int updateId = Integer.parseInt(args[2]);
                String updateDescription = args[3];
                boolean updateResult = TaskService.updateTaskById(updateId, updateDescription);

                System.out.println(updateResult ? "Task updated successfully." : "Something went wrong.");
                break;

            case "delete":
                if (args.length < 3) {
                    System.out.println("Usage: task-cli delete <id>");
                    return;
                }
                int deleteId = Integer.parseInt(args[2]);
                boolean deleteResult = TaskService.deleteTask(deleteId);

                System.out.println(deleteResult ? "Task deleted successfully." : "Something went wrong.");
                break;

            case "list":
                List<Task> tasks;
                if (args.length < 3) {
                    tasks = TaskService.loadTasks("all");
                } else {
                    tasks = TaskService.loadTasks(args[2]);
                }

                if (tasks == null || tasks.isEmpty()) {
                    System.out.println("No tasks were found.");
                } else {
                    for (Task task : tasks) {
                        System.out.println("---------------------------------------------");
                        System.out.println("ID: " + task.getId());
                        System.out.println("Description: " + task.getDescription());
                        System.out.println("Creation time: " + task.getStatus());
                        System.out.println("Creation time: " + task.getCreatedAt());
                        System.out.println("Last update time: " + task.getUpdatedAt());
                        System.out.println("---------------------------------------------");
                    }
                }
                break;

            case "mark-in-progress":
                if (args.length < 3) {
                    System.out.println("Usage: task-cli mark-in-progress <id>");
                    return;
                }
                int progressId = Integer.parseInt(args[2]);
                TaskService.updateStatus(progressId, Status.IN_PROGRESS);
                System.out.println("Task marked as in progress.");
                break;

            case "mark-done":
                if (args.length < 3) {
                    System.out.println("Usage: task-cli mark-done <id>");
                    return;
                }
                int doneId = Integer.parseInt(args[2]);
                TaskService.updateStatus(doneId, Status.COMPLETED);
                System.out.println("Task marked as done.");
                break;

            case "mark-todo":
                if (args.length < 3) {
                    System.out.println("Usage: task-cli mark-todo <id>");
                    return;
                }
                int todoId = Integer.parseInt(args[2]);
                TaskService.updateStatus(todoId, Status.PENDING);
                System.out.println("Task marked as to-do.");
                break;

            default:
                System.out.println("Invalid command.");
        }
    }
}
