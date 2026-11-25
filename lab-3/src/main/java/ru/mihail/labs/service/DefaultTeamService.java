package ru.mihail.labs.service;

import org.springframework.stereotype.Service;
import ru.mihail.labs.entity.TeamEntity;
import ru.mihail.labs.exception.TeamNotFoundException;
import ru.mihail.labs.repository.TeamRepository;

import java.util.List;

@Service
public class DefaultTeamService implements TeamService {

    private final TeamRepository repository;

    public DefaultTeamService(TeamRepository repository) {
        this.repository = repository;
    }

    @Override
    public int save(String name, int score) {
        validateFields(name, score);
        return repository.save(new TeamEntity(name.trim(), score));
    }

    @Override
    public TeamEntity findById(int id) {
        TeamEntity team = repository.findById(id);
        if (team == null) {
            throw new TeamNotFoundException("Team with id " + id + " was not found");
        }
        return team;
    }

    @Override
    public TeamEntity findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Team name must not be blank");
        }
        TeamEntity team = repository.findByName(name.trim());
        if (team == null) {
            throw new TeamNotFoundException("Team with name '" + name.trim() + "' was not found");
        }
        return team;
    }

    @Override
    public List<TeamEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public void update(TeamEntity team) {
        if (team == null) {
            throw new IllegalArgumentException("Team is required for update");
        }
        if (team.getId() == null) {
            throw new IllegalArgumentException("Team id is required for update");
        }
        validateFields(team.getName(), team.getScore());
        findById(team.getId());

        if (!repository.update(team)) {
            throw new TeamNotFoundException("Team with id " + team.getId() + " was not found");
        }
    }

    @Override
    public void deleteById(int id) {
        repository.deleteById(id);
    }

    private void validateFields(String name, int score) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Team name must not be blank");
        }
        if (score < 0) {
            throw new IllegalArgumentException("Team score must not be negative");
        }
    }
}
