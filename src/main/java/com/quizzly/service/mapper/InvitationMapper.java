package com.quizzly.service.mapper;

import com.quizzly.domain.*;
import com.quizzly.service.dto.InvitationDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Invitation} and its DTO {@link InvitationDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserAccountMapper.class })
public interface InvitationMapper extends EntityMapper<InvitationDTO, Invitation> {
    @Mapping(target = "userAccounts", source = "userAccounts", qualifiedByName = "idSet")
    InvitationDTO toDto(Invitation s);

    @Mapping(target = "removeUserAccount", ignore = true)
    Invitation toEntity(InvitationDTO invitationDTO);
}
