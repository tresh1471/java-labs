package ru.mihail.labs.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.mihail.labs.entity.TeamEntity;
import ru.mihail.labs.repository.TeamRepository;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Import(TeamControllerTest.MockRepositoryConfiguration.class)
class TeamControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TeamRepository repository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        reset(repository);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void createReturns201AndGeneratedId() throws Exception {
        when(repository.save(any(TeamEntity.class))).thenReturn(7);

        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Java Team","score":25}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(7));
    }

    @Test
    void createWithBlankNameReturns400() throws Exception {
        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":" ","score":25}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("validation.error"));
    }

    @Test
    void findByIdReturnsTeam() throws Exception {
        when(repository.findById(7)).thenReturn(new TeamEntity(7, "Java Team", 25));

        mockMvc.perform(get("/api/teams/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Java Team"))
                .andExpect(jsonPath("$.score").value(25));
    }

    @Test
    void findByIdReturns404WhenTeamDoesNotExist() throws Exception {
        when(repository.findById(404)).thenReturn(null);

        mockMvc.perform(get("/api/teams/404"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("team.not-found"));
    }

    @Test
    void findByNameReturnsTeam() throws Exception {
        when(repository.findByName("Java Team")).thenReturn(new TeamEntity(7, "Java Team", 25));

        mockMvc.perform(get("/api/teams/search").param("name", "Java Team"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7));
    }

    @Test
    void findAllReturnsTeams() throws Exception {
        when(repository.findAll()).thenReturn(List.of(new TeamEntity(7, "Java Team", 25)));

        mockMvc.perform(get("/api/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Java Team"));
    }

    @Test
    void updateReturns204() throws Exception {
        when(repository.findById(7)).thenReturn(new TeamEntity(7, "Old Name", 10));
        when(repository.update(any(TeamEntity.class))).thenReturn(true);

        mockMvc.perform(put("/api/teams/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Updated Team","score":30}
                                """))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteReturns204() throws Exception {
        mockMvc.perform(delete("/api/teams/7"))
                .andExpect(status().isNoContent());
    }

    @TestConfiguration
    static class MockRepositoryConfiguration {

        @Bean
        TeamRepository teamRepository() {
            return mock(TeamRepository.class);
        }
    }
}
