package com.quizzly.service.mapper;

import com.quizzly.domain.*;
import com.quizzly.service.dto.QuestionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Question} and its DTO {@link QuestionDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuizMapper.class })
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {
    @Mapping(target = "quiz", source = "quiz", qualifiedByName = "id")
    QuestionDTO toDto(Question s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuestionDTO toDtoId(Question question);
}
