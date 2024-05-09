package ru.petrsushilin.testapp.requestservice.requests;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.petrsushilin.testapp.requestservice.requests.dto.RequestCreationDTO;
import ru.petrsushilin.testapp.requestservice.requests.dto.RequestMessageDTO;
import ru.petrsushilin.testapp.requestservice.requests.dto.RequestResponseDTO;
import ru.petrsushilin.testapp.requestservice.requests.dto.RequestSetStageDTO;

/**
 * @author Petr Sushilin
 * @version 1.0
 * @since 05.05.2024
 */
@Mapper(componentModel = "requestService")
public interface RequestMapper {
    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "stage", ignore = true)
    Request toEntity(RequestCreationDTO dto);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "stage", ignore = true)
    Request toEntity(RequestMessageDTO dto);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "description", ignore = true)
    Request toEntity (RequestSetStageDTO requestSetStageDTO);

    @Mapping(target = "requestID", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "userID", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "userSurname", source = "user.surname")
    @Mapping(target = "stage", source = "stage.name")
    @Mapping(target = "description", source = "description")
    RequestResponseDTO toRequestResponseDTO(Request entity);
}
