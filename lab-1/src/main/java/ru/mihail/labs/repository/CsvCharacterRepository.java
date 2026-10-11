package ru.mihail.labs.repository;

import ru.mihail.labs.entity.CharacterEntity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CsvCharacterRepository {

    private static final String HEADER =
            "id,name,status,species,type,gender,origin/name,location/name,created";
    private static final int COLUMNS_COUNT = 9;

    private final Path csvFile;

    public CsvCharacterRepository(Path csvFile) {
        this.csvFile = csvFile;
    }

    public int create(CharacterEntity character) {
        List<CharacterEntity> characters = findAll();
        int nextId = characters.stream()
                .mapToInt(CharacterEntity::getId)
                .max()
                .orElse(0) + 1;

        character.setId(nextId);
        characters.add(character);
        writeAll(characters);
        return nextId;
    }

    public Optional<CharacterEntity> findById(int id) {
        return findAll().stream()
                .filter(character -> character.getId() == id)
                .findFirst();
    }

    public List<CharacterEntity> findAll() {
        List<CharacterEntity> characters = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(csvFile, StandardCharsets.UTF_8)) {
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    characters.add(fromCsv(line));
                }
            }
            return characters;
        } catch (IOException e) {
            throw new RuntimeException("Не получилось прочитать CSV-файл: " + csvFile, e);
        }
    }

    public boolean update(CharacterEntity updatedCharacter) {
        if (updatedCharacter.getId() == null) {
            throw new IllegalArgumentException("Для обновления персонажа нужен id");
        }

        List<CharacterEntity> characters = findAll();
        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i).getId().equals(updatedCharacter.getId())) {
                characters.set(i, updatedCharacter);
                writeAll(characters);
                return true;
            }
        }
        return false;
    }

    public boolean deleteById(int id) {
        List<CharacterEntity> characters = findAll();
        boolean removed = characters.removeIf(character -> character.getId() == id);
        if (removed) {
            writeAll(characters);
        }
        return removed;
    }

    private CharacterEntity fromCsv(String line) {
        String[] columns = line.split(",", -1);
        if (columns.length != COLUMNS_COUNT) {
            throw new IllegalArgumentException("Некорректная строка CSV: " + line);
        }

        return new CharacterEntity(
                Integer.parseInt(columns[0]),
                columns[1],
                columns[2],
                columns[3],
                columns[4],
                columns[5],
                columns[6],
                columns[7],
                columns[8]
        );
    }

    private void writeAll(List<CharacterEntity> characters) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                csvFile,
                StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
        )) {
            writer.write(HEADER);
            for (CharacterEntity character : characters) {
                writer.write('\n');
                writer.write(toCsv(character));
            }
        } catch (IOException e) {
            throw new RuntimeException("Не получилось записать CSV-файл: " + csvFile, e);
        }
    }

    private String toCsv(CharacterEntity character) {
        return String.join(",",
                character.getId().toString(),
                character.getName(),
                character.getStatus(),
                character.getSpecies(),
                character.getType(),
                character.getGender(),
                character.getOrigin(),
                character.getLocation(),
                character.getCreated()
        );
    }
}
