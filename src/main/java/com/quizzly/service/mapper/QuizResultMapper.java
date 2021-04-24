package com.quizzly.service.mapper;

import com.quizzly.domain.*;
import com.quizzly.service.dto.QuizResultDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuizResult} and its DTO {@link QuizResultDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuizMapper.class, UserAccountMapper.class })
public interface QuizResultMapper extends EntityMapper<QuizResultDTO, QuizResult> {
    @Mapping(target = "quiz", source = "quiz", qualifiedByName = "id")
    @Mapping(target = "quizResult", source = "quizResult", qualifiedByName = "id")
    QuizResultDTO toDto(QuizResult s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuizResultDTO toDtoId(QuizResult quizResult);
}
