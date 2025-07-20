package t1.workshop4.SyntheticHumanCoreStarter.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import t1.workshop4.SyntheticHumanCoreStarter.dto.CommandDTO;

@RestController
@RequestMapping("/api/commands")
public class CommandController {
  @PostMapping
  public ResponseEntity<String> receiveCommand(@Valid @RequestBody CommandDTO command) {
    return ResponseEntity.ok("Command received: " + command.description());
  }
}