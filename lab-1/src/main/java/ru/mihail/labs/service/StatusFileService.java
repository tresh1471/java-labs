package ru.mihail.labs.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeSet;

public class StatusFileService {
    public TreeSet<String> readStatuses(String fileName) {
        TreeSet<String> statuses = new TreeSet<>();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("Файл не найден: " + fileName);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",", -1);
                String status = columns[2];
                statuses.add(status);
            }
        } catch (IOException e) {
            throw new RuntimeException("Не получилось прочитать файл: " + fileName, e);
        }

        return statuses;
    }

    public void writeStatuses(Path resultFile, TreeSet<String> statuses) {
        try (BufferedWriter writer = Files.newBufferedWriter(resultFile, StandardCharsets.UTF_8)) {
            for (String status : statuses) {
                writer.write(status);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Не получилось записать файл: " + resultFile, e);
        }
    }
}
