package ru.mihail.labs.entity;

import java.util.Objects;

public class TeamEntity {

    private Integer id;
    private String name;
    private int score;

    public TeamEntity(String name, int score) {
        this(null, name, score);
    }

    public TeamEntity(Integer id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof TeamEntity team)) {
            return false;
        }
        return score == team.score && Objects.equals(id, team.id) && Objects.equals(name, team.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, score);
    }

    @Override
    public String toString() {
        return "TeamEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}
