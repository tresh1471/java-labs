package ru.mihail.labs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mihail.labs.config.ConnectionFactory;
import ru.mihail.labs.config.DatabaseConfig;
import ru.mihail.labs.config.DatabaseMigrator;
import ru.mihail.labs.entity.TeamEntity;
import ru.mihail.labs.repository.JdbcTeamRepository;
import ru.mihail.labs.repository.TeamRepository;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        DatabaseConfig databaseConfig = DatabaseConfig.load("application.properties");
        new DatabaseMigrator(databaseConfig).migrate();

        try (ConnectionFactory connectionFactory = new ConnectionFactory(databaseConfig)) {
            TeamRepository repository = new JdbcTeamRepository(connectionFactory);
            demonstrateCrud(repository);
        }
    }

    private static void demonstrateCrud(TeamRepository repository) {
        String teamName = "Java Team " + System.currentTimeMillis();
        TeamEntity team = new TeamEntity(teamName, 10);

        int id = repository.save(team);
        log.info("save -> id={}", id);

        TeamEntity foundById = repository.findById(id);
        log.info("findById -> {}", foundById);

        TeamEntity foundByName = repository.findByName(teamName);
        log.info("findByName -> {}", foundByName);

        log.info("findAll -> {}", repository.findAll());

        team.setScore(25);
        boolean updated = repository.update(team);
        log.info("update -> {}, entity={}", updated, repository.findById(id));

        repository.deleteById(id);
        log.info("deleteById -> findById returns {}", repository.findById(id));
    }
}
