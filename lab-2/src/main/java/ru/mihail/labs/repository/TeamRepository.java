package ru.mihail.labs.repository;

import ru.mihail.labs.entity.TeamEntity;

import java.util.List;

public interface TeamRepository {

    int save(TeamEntity team);

    TeamEntity findById(int id);

    TeamEntity findByName(String name);

    List<TeamEntity> findAll();

    boolean update(TeamEntity team);

    void deleteById(int id);
}
