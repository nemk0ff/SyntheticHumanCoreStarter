package t1.workshop4.SyntheticHumanCoreStarter.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import t1.workshop4.SyntheticHumanCoreStarter.model.CommandPriority;

public record CommandDTO(
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    String description,

    @NotNull(message = "Priority cannot be null")
    CommandPriority priority,

    @Size(max = 100, message = "Author must be less than 100 characters")
    String author,

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z$",
        message = "Time must be in ISO-8601 format")
    String time) {
}
