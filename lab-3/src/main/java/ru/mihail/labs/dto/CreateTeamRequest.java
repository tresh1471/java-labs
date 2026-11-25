package ru.mihail.labs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CreateTeamRequest(
        @NotBlank(message = "Team name must not be blank")
        @Size(max = 255, message = "Team name must contain at most 255 characters")
        String name,

        @NotNull(message = "Team score is required")
        @PositiveOrZero(message = "Team score must not be negative")
        Integer score
) {
}
