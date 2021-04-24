package com.quizzly.service;

import com.quizzly.service.dto.QuestionAnswerDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.quizzly.domain.QuestionAnswer}.
 */
public interface QuestionAnswerService {
    /**
     * Save a questionAnswer.
     *
     * @param questionAnswerDTO the entity to save.
     * @return the persisted entity.
     */
    QuestionAnswerDTO save(QuestionAnswerDTO questionAnswerDTO);

    /**
     * Partially updates a questionAnswer.
     *
     * @param questionAnswerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuestionAnswerDTO> partialUpdate(QuestionAnswerDTO questionAnswerDTO);

    /**
     * Get all the questionAnswers.
     *
     * @return the list of entities.
     */
    List<QuestionAnswerDTO> findAll();

    /**
     * Get the "id" questionAnswer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuestionAnswerDTO> findOne(Long id);

    /**
     * Delete the "id" questionAnswer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
