package com.quizzly.service.mapper;

import com.quizzly.domain.*;
import com.quizzly.service.dto.QuestionAnswerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuestionAnswer} and its DTO {@link QuestionAnswerDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuestionMapper.class, UserAccountMapper.class, QuizRezultMapper.class })
public interface QuestionAnswerMapper extends EntityMapper<QuestionAnswerDTO, QuestionAnswer> {
    @Mapping(target = "question", source = "question", qualifiedByName = "id")
    @Mapping(target = "participant", source = "participant", qualifiedByName = "id")
    @Mapping(target = "rezult", source = "rezult", qualifiedByName = "id")
    QuestionAnswerDTO toDto(QuestionAnswer s);
}
