package ru.mihail.labs.repository;

import ru.mihail.labs.config.ConnectionFactory;
import ru.mihail.labs.entity.TeamEntity;
import ru.mihail.labs.exception.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcTeamRepository implements TeamRepository {

    private static final String INSERT_SQL = "INSERT INTO team (name, score) VALUES (?, ?)";
    private static final String FIND_BY_ID_SQL = "SELECT id, name, score FROM team WHERE id = ?";
    private static final String FIND_BY_NAME_SQL = "SELECT id, name, score FROM team WHERE name = ?";
    private static final String FIND_ALL_SQL = "SELECT id, name, score FROM team ORDER BY id";
    private static final String UPDATE_SQL = "UPDATE team SET name = ?, score = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM team WHERE id = ?";

    private final ConnectionFactory connectionFactory;

    public JdbcTeamRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public int save(TeamEntity team) {
        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        INSERT_SQL,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {
            statement.setString(1, team.getName());
            statement.setInt(2, team.getScore());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (!resultSet.next()) {
                    throw new DatabaseException("Database did not return an id for the saved team", null);
                }
                int generatedId = resultSet.getInt(1);
                team.setId(generatedId);
                return generatedId;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save team", e);
        }
    }

    @Override
    public TeamEntity findById(int id) {
        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)
        ) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? mapTeam(resultSet) : null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find team by id: " + id, e);
        }
    }

    @Override
    public TeamEntity findByName(String name) {
        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_BY_NAME_SQL)
        ) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? mapTeam(resultSet) : null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find team by name: " + name, e);
        }
    }

    @Override
    public List<TeamEntity> findAll() {
        List<TeamEntity> teams = new ArrayList<>();

        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                teams.add(mapTeam(resultSet));
            }
            return teams;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find all teams", e);
        }
    }

    @Override
    public boolean update(TeamEntity team) {
        if (team.getId() == null) {
            throw new IllegalArgumentException("Team id is required for update");
        }

        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)
        ) {
            statement.setString(1, team.getName());
            statement.setInt(2, team.getScore());
            statement.setInt(3, team.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update team with id: " + team.getId(), e);
        }
    }

    @Override
    public void deleteById(int id) {
        try (
                Connection connection = connectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_SQL)
        ) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete team with id: " + id, e);
        }
    }

    private TeamEntity mapTeam(ResultSet resultSet) throws SQLException {
        return new TeamEntity(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getInt("score")
        );
    }
}
