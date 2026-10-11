package ru.mihail.labs.config;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mihail.labs.exception.DatabaseException;

public class DatabaseMigrator {

    private static final Logger log = LoggerFactory.getLogger(DatabaseMigrator.class);

    private final DatabaseConfig databaseConfig;

    public DatabaseMigrator(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public void migrate() {
        try {
            Flyway flyway = Flyway.configure()
                    .dataSource(databaseConfig.getUrl(), databaseConfig.getUsername(), databaseConfig.getPassword())
                    .schemas(databaseConfig.getSchema())
                    .defaultSchema(databaseConfig.getSchema())
                    .createSchemas(true)
                    .locations("classpath:db/migration")
                    .load();

            int migrationsCount = flyway.migrate().migrationsExecuted;
            log.info("Database migrations applied: {}", migrationsCount);
        } catch (Exception e) {
            throw new DatabaseException("Failed to apply database migrations", e);
        }
    }
}
