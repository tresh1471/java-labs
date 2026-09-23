package ru.mihail.labs;

import ru.mihail.labs.service.StatusFileService;

import java.nio.file.Path;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        StatusFileService statusFileService = new StatusFileService();
        TreeSet<String> statuses = statusFileService.readStatuses("characters.csv");

        Path resultFile = Path.of("statuses.txt");
        statusFileService.writeStatuses(resultFile, statuses);

        System.out.println("Уникальные статусы:");
        for (String status : statuses) {
            System.out.println(status);
        }
        System.out.println("Результат записан в файл: " + resultFile.toAbsolutePath());
    }
}
