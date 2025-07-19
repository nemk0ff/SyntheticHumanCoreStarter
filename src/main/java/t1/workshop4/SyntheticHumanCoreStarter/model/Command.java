package t1.workshop4.SyntheticHumanCoreStarter.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Command {
  @Size(max = 1000, message = "Description must be less than 1000 characters")
  private String description;

  @NotNull(message = "Priority cannot be null")
  private CommandPriority priority;

  @Size(max = 100, message = "Author must be less than 100 characters")
  private String author;

  @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z$",
      message = "Time must be in ISO-8601 format")
  private String time;
}