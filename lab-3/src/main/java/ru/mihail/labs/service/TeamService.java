package ru.mihail.labs.service;

import ru.mihail.labs.entity.TeamEntity;

import java.util.List;

public interface TeamService {

    int save(String name, int score);

    TeamEntity findById(int id);

    TeamEntity findByName(String name);

    List<TeamEntity> findAll();

    void update(TeamEntity team);

    void deleteById(int id);
}
