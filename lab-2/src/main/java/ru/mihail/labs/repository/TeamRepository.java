//интерфейс + определение того какие команды должны уметь выполнять

package ru.mihail.labs.repository;

//можно использовать класс из другого пакета по короткому имени
import ru.mihail.labs.entity.TeamEntity;

//добавляем List для динамического изменения списка
import java.util.List;

public interface TeamRepository {
//сохранить команду
    int save(TeamEntity team);
//найти команду по id
    TeamEntity findById(int id);
//найти комнаду по названию
    TeamEntity findByName(String name);
//получить список всех команд
    List<TeamEntity> findAll();
//изменить команду
    boolean update(TeamEntity team);
//удалить команду по id
    void deleteById(int id);
}
