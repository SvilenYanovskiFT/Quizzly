package com.quizzly.service.mapper;

import com.quizzly.domain.*;
import com.quizzly.service.dto.QuestionCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuestionCategory} and its DTO {@link QuestionCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QuestionCategoryMapper extends EntityMapper<QuestionCategoryDTO, QuestionCategory> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuestionCategoryDTO toDtoId(QuestionCategory questionCategory);
}
