package t1.workshop4.SyntheticHumanCoreStarter.services;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Service;
import t1.workshop4.SyntheticHumanCoreStarter.dto.CommandDTO;
import t1.workshop4.SyntheticHumanCoreStarter.dto.CommandMapper;
import t1.workshop4.SyntheticHumanCoreStarter.exceptions.CommandQueueFullException;
import t1.workshop4.SyntheticHumanCoreStarter.model.Command;
import t1.workshop4.SyntheticHumanCoreStarter.model.CommandPriority;

@Service
public class CommandProcessor {
  private final AtomicInteger queueSize = new AtomicInteger();
  private final Counter commonCommandsCounter = Metrics.counter("command.common");
  private final Counter criticalCommandsCounter = Metrics.counter("command.critical");
  private static final Logger logger = LoggerFactory.getLogger(CommandProcessor.class);
  private final BlockingQueue<Command> commandQueue = new ArrayBlockingQueue<>(10);

  @PostConstruct
  public void init() {
    new Thread(this::processQueue).start();
  }

  public void addCommand(CommandDTO commandDto) {
    Command command = CommandMapper.INSTANCE.toCommand(commandDto);

    if (command.getPriority() == CommandPriority.CRITICAL) {
      criticalCommandsCounter.increment();
      executeCommandImmediately(command);
    } else {
      commonCommandsCounter.increment();
      queueSize.incrementAndGet();
      if (!commandQueue.offer(command)) {
        queueSize.decrementAndGet();
        throw new CommandQueueFullException("Command queue is full");
      }
    }
  }

  private void processQueue() {
    while (true) {
      try {
        Command command = commandQueue.take();
        executeCommand(command);
        queueSize.decrementAndGet();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
  }

  private void executeCommand(Command command) {
    logger.info("Executing command: {}", command);
    // Имитация работы
    try {
      Thread.sleep(1000);
    } catch (InterruptedException ignored) {
    }
  }

  private void executeCommandImmediately(Command command) {
    logger.info("Executing CRITICAL command immediately: {}", command);
  }

  @ReadOperation
  public int getQueueSize() {
    return queueSize.get();
  }
}