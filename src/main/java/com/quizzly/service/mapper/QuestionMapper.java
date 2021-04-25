package com.quizzly.service.mapper;

import com.quizzly.domain.*;
import com.quizzly.service.dto.QuestionDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Question} and its DTO {@link QuestionDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuestionCategoryMapper.class, UserAccountMapper.class })
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {
    @Mapping(target = "questionCategory", source = "questionCategory", qualifiedByName = "id")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "id")
    QuestionDTO toDto(Question s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuestionDTO toDtoId(Question question);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<QuestionDTO> toDtoIdSet(Set<Question> question);
}
