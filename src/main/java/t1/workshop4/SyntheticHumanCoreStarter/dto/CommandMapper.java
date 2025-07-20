package t1.workshop4.SyntheticHumanCoreStarter.dto;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import t1.workshop4.SyntheticHumanCoreStarter.model.Command;

@Mapper(componentModel = "spring", uses = Command.class)
public interface CommandMapper {

  @Mapping(target = "members", source = "members")
  CommandDTO toDto(Command command);

  List<CommandDTO> toListDto(List<Command> commands);
}
