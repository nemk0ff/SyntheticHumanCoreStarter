package t1.workshop4.SyntheticHumanCoreStarter.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import t1.workshop4.SyntheticHumanCoreStarter.dto.CommandDTO;
import t1.workshop4.SyntheticHumanCoreStarter.services.CommandProcessor;
import t1.workshop4.SyntheticHumanCoreStarter.services.TestService;

@RestController
@RequestMapping("/api/commands")
public class CommandController {
  private static final Logger log = LoggerFactory.getLogger(CommandController.class);
  private final CommandProcessor commandProcessor;
  private final TestService testService;

  public CommandController(CommandProcessor commandProcessor, TestService testService) {
    this.commandProcessor = commandProcessor;
    this.testService = testService;
  }

  @PostMapping
  public ResponseEntity<String> receiveCommand(@Valid @RequestBody CommandDTO command) {
    commandProcessor.addCommand(command);
    log.info(testService.testMethod(command.description()));
    return ResponseEntity.ok("Command received");
  }
}