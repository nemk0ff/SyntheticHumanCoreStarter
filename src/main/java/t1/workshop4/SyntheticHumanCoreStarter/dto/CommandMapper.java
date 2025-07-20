package t1.workshop4.SyntheticHumanCoreStarter.dto;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import t1.workshop4.SyntheticHumanCoreStarter.model.Command;

@Mapper
public interface CommandMapper {
  CommandMapper INSTANCE = Mappers.getMapper(CommandMapper.class);

  CommandDTO toDto(Command command);

  List<CommandDTO> toListDto(List<Command> commands);

  Command toCommand(CommandDTO commandDTO);
}
