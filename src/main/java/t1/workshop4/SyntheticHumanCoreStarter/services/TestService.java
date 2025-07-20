package t1.workshop4.SyntheticHumanCoreStarter.services;

import org.springframework.stereotype.Service;
import t1.workshop4.SyntheticHumanCoreStarter.annotations.WeylandWatchingYou;

@Service
public class TestService {
  @WeylandWatchingYou
  public String testMethod(String param) {
    return "Processed: " + param;
  }
}