package ru.vrannir.streamapi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        List<Task> tasks = new ArrayList<>(List.of(
                new Task("123", "first", TaskStatus.OPEN),
                new Task("543p", "second", TaskStatus.INWORK),
                new Task("sdfg112", "third", TaskStatus.CLOSED),
                new Task("1278g", "fourth", TaskStatus.OPEN),
                new Task("sdafsd", "The last", TaskStatus.INWORK)
        )
        );

        System.out.println(selectTask(tasks, TaskStatus.OPEN));
        System.out.println(getSortTaskList(tasks));
        System.out.println(checkTaskID(tasks, "sdfg1s12"));
        System.out.println(countTasksByStatus(tasks, TaskStatus.INWORK));
    }

    public static List<Task> selectTask(List<Task> tasks, TaskStatus taskStatus) {
        return tasks.stream()
                .filter(task -> task.getStatus() == taskStatus)
                .toList();
    }

    public static boolean checkTaskID(List<Task> tasks, String id) {
        return tasks.stream()
                .anyMatch(task -> task.getId().equals(id));
    }

    public static List<Task> getSortTaskList(List<Task> tasks) {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getStatus))
                .toList();
    }

    public static long countTasksByStatus(List<Task> tasks, TaskStatus taskStatus) {
        return tasks.stream()
                .filter(task -> task.getStatus() == taskStatus)
                .count();
    }


}
