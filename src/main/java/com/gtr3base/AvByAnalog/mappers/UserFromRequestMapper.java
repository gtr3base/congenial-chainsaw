package com.gtr3base.AvByAnalog.mappers;

import com.gtr3base.AvByAnalog.dto.RegisterRequest;
import com.gtr3base.AvByAnalog.entity.User;
import com.gtr3base.AvByAnalog.enums.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = UserRole.class)
public interface UserFromRequestMapper {
    UserFromRequestMapper INSTANCE = Mappers.getMapper(UserFromRequestMapper.class);

    RegisterRequest toDTO(User entity);

    @Mapping(target = "role", expression = "java(UserRole.USER)")
    @Mapping(target = "password", ignore = true)
    User toUser(RegisterRequest registerRequest);
}
