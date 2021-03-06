package com.quizzly.service.mapper;

import com.quizzly.domain.*;
import com.quizzly.service.dto.QuizDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Quiz} and its DTO {@link QuizDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuestionMapper.class, UserAccountMapper.class })
public interface QuizMapper extends EntityMapper<QuizDTO, Quiz> {
    @Mapping(target = "questions", source = "questions", qualifiedByName = "idSet")
    @Mapping(target = "owner", source = "owner", qualifiedByName = "id")
    QuizDTO toDto(Quiz s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuizDTO toDtoId(Quiz quiz);

    @Mapping(target = "removeQuestion", ignore = true)
    Quiz toEntity(QuizDTO quizDTO);
}
