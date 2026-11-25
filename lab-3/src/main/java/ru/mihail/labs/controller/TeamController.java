package ru.mihail.labs.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mihail.labs.dto.CreateTeamRequest;
import ru.mihail.labs.dto.CreatedTeamResponse;
import ru.mihail.labs.dto.UpdateTeamRequest;
import ru.mihail.labs.entity.TeamEntity;
import ru.mihail.labs.service.TeamService;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private static final Logger log = LoggerFactory.getLogger(TeamController.class);

    private final TeamService service;

    public TeamController(TeamService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CreatedTeamResponse> create(@Valid @RequestBody CreateTeamRequest request) {
        log.info("Creating team with name {}", request.name());
        int id = service.save(request.name(), request.score());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreatedTeamResponse(id));
    }

    @GetMapping("/{id}")
    public TeamEntity findById(@PathVariable int id) {
        return service.findById(id);
    }

    @GetMapping("/search")
    public TeamEntity findByName(@RequestParam String name) {
        return service.findByName(name);
    }

    @GetMapping
    public List<TeamEntity> findAll() {
        return service.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable int id,
            @Valid @RequestBody UpdateTeamRequest request
    ) {
        service.update(new TeamEntity(id, request.name(), request.score()));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
