package ru.mihail.labs.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mihail.labs.entity.TeamEntity;
import ru.mihail.labs.exception.TeamNotFoundException;
import ru.mihail.labs.repository.TeamRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultTeamServiceTest {

    private static final int TEAM_ID = 7;
    private static final String TEAM_NAME = "Java Team";
    private static final int TEAM_SCORE = 25;

    @Mock
    private TeamRepository repository;

    @InjectMocks
    private DefaultTeamService service;

    @Test
    void saveCreatesTeamAndReturnsGeneratedId() {
        TeamEntity teamWithoutId = new TeamEntity(TEAM_NAME, TEAM_SCORE);
        when(repository.save(teamWithoutId)).thenReturn(TEAM_ID);

        int result = service.save(TEAM_NAME, TEAM_SCORE);

        assertEquals(TEAM_ID, result);
        verify(repository).save(teamWithoutId);
    }

    @Test
    void saveRejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> service.save("  ", TEAM_SCORE));
    }

    @Test
    void saveRejectsNegativeScore() {
        assertThrows(IllegalArgumentException.class, () -> service.save(TEAM_NAME, -1));
    }

    @Test
    void findByIdReturnsExistingTeam() {
        TeamEntity expected = new TeamEntity(TEAM_ID, TEAM_NAME, TEAM_SCORE);
        when(repository.findById(TEAM_ID)).thenReturn(expected);

        TeamEntity result = service.findById(TEAM_ID);

        assertSame(expected, result);
    }

    @Test
    void findByIdThrowsWhenTeamDoesNotExist() {
        when(repository.findById(TEAM_ID)).thenReturn(null);

        assertThrows(TeamNotFoundException.class, () -> service.findById(TEAM_ID));
    }

    @Test
    void findByNameReturnsExistingTeam() {
        TeamEntity expected = new TeamEntity(TEAM_ID, TEAM_NAME, TEAM_SCORE);
        when(repository.findByName(TEAM_NAME)).thenReturn(expected);

        TeamEntity result = service.findByName(TEAM_NAME);

        assertSame(expected, result);
    }

    @Test
    void findByNameThrowsWhenTeamDoesNotExist() {
        when(repository.findByName(TEAM_NAME)).thenReturn(null);

        assertThrows(TeamNotFoundException.class, () -> service.findByName(TEAM_NAME));
    }

    @Test
    void findAllReturnsRepositoryResult() {
        List<TeamEntity> expected = List.of(new TeamEntity(TEAM_ID, TEAM_NAME, TEAM_SCORE));
        when(repository.findAll()).thenReturn(expected);

        List<TeamEntity> result = service.findAll();

        assertSame(expected, result);
    }

    @Test
    void updateUpdatesExistingTeam() {
        TeamEntity team = new TeamEntity(TEAM_ID, TEAM_NAME, TEAM_SCORE);
        when(repository.findById(TEAM_ID)).thenReturn(team);
        when(repository.update(team)).thenReturn(true);

        service.update(team);

        verify(repository).update(team);
    }

    @Test
    void updateThrowsWhenTeamDoesNotExist() {
        TeamEntity team = new TeamEntity(TEAM_ID, TEAM_NAME, TEAM_SCORE);
        when(repository.findById(TEAM_ID)).thenReturn(null);

        assertThrows(TeamNotFoundException.class, () -> service.update(team));
    }

    @Test
    void updateThrowsWhenTeamDisappearsDuringUpdate() {
        TeamEntity team = new TeamEntity(TEAM_ID, TEAM_NAME, TEAM_SCORE);
        when(repository.findById(TEAM_ID)).thenReturn(team);
        when(repository.update(team)).thenReturn(false);

        assertThrows(TeamNotFoundException.class, () -> service.update(team));
    }

    @Test
    void updateRejectsTeamWithoutId() {
        TeamEntity team = new TeamEntity(TEAM_NAME, TEAM_SCORE);

        assertThrows(IllegalArgumentException.class, () -> service.update(team));
    }

    @Test
    void deleteByIdDelegatesToRepository() {
        service.deleteById(TEAM_ID);

        verify(repository).deleteById(TEAM_ID);
    }
}
