package com.quizzly.service.mapper;

import com.quizzly.domain.*;
import com.quizzly.service.dto.QuizRezultDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuizRezult} and its DTO {@link QuizRezultDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuizMapper.class, UserAccountMapper.class })
public interface QuizRezultMapper extends EntityMapper<QuizRezultDTO, QuizRezult> {
    @Mapping(target = "quiz", source = "quiz", qualifiedByName = "id")
    @Mapping(target = "quizRezult", source = "quizRezult", qualifiedByName = "id")
    QuizRezultDTO toDto(QuizRezult s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuizRezultDTO toDtoId(QuizRezult quizRezult);
}
