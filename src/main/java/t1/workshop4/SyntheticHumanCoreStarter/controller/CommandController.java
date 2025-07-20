package t1.workshop4.SyntheticHumanCoreStarter.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import t1.workshop4.SyntheticHumanCoreStarter.dto.CommandDTO;
import t1.workshop4.SyntheticHumanCoreStarter.services.CommandProcessor;

@RestController
@RequestMapping("/api/commands")
public class CommandController {
  private final CommandProcessor commandProcessor;

  public CommandController(CommandProcessor commandProcessor) {
    this.commandProcessor = commandProcessor;
  }

  @PostMapping
  public ResponseEntity<String> receiveCommand(@Valid @RequestBody CommandDTO command) {
    commandProcessor.addCommand(command);
    return ResponseEntity.ok("Command received");
  }
}