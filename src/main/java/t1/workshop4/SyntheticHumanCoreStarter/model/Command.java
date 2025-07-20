package t1.workshop4.SyntheticHumanCoreStarter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Command {
  private String description;

  private CommandPriority priority;

  private String author;

  private String time;
}