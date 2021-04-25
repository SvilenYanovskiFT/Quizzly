package com.quizzly.service;

import com.quizzly.service.dto.QuestionCategoryDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.quizzly.domain.QuestionCategory}.
 */
public interface QuestionCategoryService {
    /**
     * Save a questionCategory.
     *
     * @param questionCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    QuestionCategoryDTO save(QuestionCategoryDTO questionCategoryDTO);

    /**
     * Partially updates a questionCategory.
     *
     * @param questionCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuestionCategoryDTO> partialUpdate(QuestionCategoryDTO questionCategoryDTO);

    /**
     * Get all the questionCategories.
     *
     * @return the list of entities.
     */
    List<QuestionCategoryDTO> findAll();

    /**
     * Get the "id" questionCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuestionCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" questionCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
