package com.gtr3base.AvByAnalog.mappers;

import com.gtr3base.AvByAnalog.dto.RegisterRequest;
import com.gtr3base.AvByAnalog.entity.User;
import com.gtr3base.AvByAnalog.enums.UserRole;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, imports = UserRole.class)
public interface UserFromRequestMapper {

    RegisterRequest toDTO(User entity);

    @Mapping(target = "role", expression = "java(UserRole.USER)")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "cars", ignore = true)
    @Mapping(target = "favorites", ignore = true)
    User toUser(RegisterRequest registerRequest);
}
