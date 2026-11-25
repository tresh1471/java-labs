//сущность (2 конструктора)

package ru.mihail.labs.entity;

public class TeamEntity {

    private Integer id;
    private String name;
    private int score;

    //1 конструктор для создания существа через new
    public TeamEntity(String name, int score) {
        this(null, name, score);
    }

    //2 конструктор присваивает значения полям объекта
    public TeamEntity(Integer id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    //меняем\выводим информацию сущности
    //вызов set не сохранит значение, его необходимо перезаписать повторным вызовом repository.update(team)
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

    //преобразуем в строки для вывода
    @Override
    public String toString() {
        return "TeamEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}
