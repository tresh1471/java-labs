package ru.mihail.labs;

import ru.mihail.labs.entity.CharacterEntity;
import ru.mihail.labs.repository.CsvCharacterRepository;
import ru.mihail.labs.service.StatusFileService;

import java.nio.file.Path;
import java.time.Instant;
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

        demonstrateCrud();
    }

    private static void demonstrateCrud() {
        Path charactersFile = Path.of("lab-1", "src", "main", "resources", "characters.csv");
        CsvCharacterRepository repository = new CsvCharacterRepository(charactersFile);

        CharacterEntity character = new CharacterEntity(
                null,
                "Laboratory Character",
                "Alive",
                "Human",
                "Student",
                "unknown",
                "Earth",
                "Java laboratory",
                Instant.now().toString()
        );

        int id = repository.create(character);
        System.out.println("Create: " + repository.findById(id).orElseThrow());
        System.out.println("Read all, количество персонажей: " + repository.findAll().size());

        character.setStatus("Dead");
        System.out.println("Update: " + repository.update(character));
        System.out.println("Read: " + repository.findById(id).orElseThrow());

        System.out.println("Delete: " + repository.deleteById(id));
        System.out.println("После удаления: " + repository.findById(id));
    }
}
