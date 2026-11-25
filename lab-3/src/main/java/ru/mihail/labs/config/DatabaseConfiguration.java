package ru.mihail.labs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import ru.mihail.labs.repository.JdbcTeamRepository;
import ru.mihail.labs.repository.TeamRepository;

@Configuration
@Profile("!test")
public class DatabaseConfiguration {

    @Bean
    public DatabaseConfig databaseConfig() {
        return DatabaseConfig.load("application.properties");
    }

    @Bean(initMethod = "migrate")
    public DatabaseMigrator databaseMigrator(DatabaseConfig databaseConfig) {
        return new DatabaseMigrator(databaseConfig);
    }

    @Bean(destroyMethod = "close")
    @DependsOn("databaseMigrator")
    public ConnectionFactory connectionFactory(DatabaseConfig databaseConfig) {
        return new ConnectionFactory(databaseConfig);
    }

    @Bean
    public TeamRepository teamRepository(ConnectionFactory connectionFactory) {
        return new JdbcTeamRepository(connectionFactory);
    }

}
