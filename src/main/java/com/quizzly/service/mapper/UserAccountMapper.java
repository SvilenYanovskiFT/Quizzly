package com.quizzly.service.mapper;

import com.quizzly.domain.*;
import com.quizzly.service.dto.UserAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAccount} and its DTO {@link UserAccountDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface UserAccountMapper extends EntityMapper<UserAccountDTO, UserAccount> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    UserAccountDTO toDto(UserAccount s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserAccountDTO toDtoId(UserAccount userAccount);
}
