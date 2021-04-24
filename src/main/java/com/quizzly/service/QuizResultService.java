package com.quizzly.service;

import com.quizzly.service.dto.QuizResultDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.quizzly.domain.QuizResult}.
 */
public interface QuizResultService {
    /**
     * Save a quizResult.
     *
     * @param quizResultDTO the entity to save.
     * @return the persisted entity.
     */
    QuizResultDTO save(QuizResultDTO quizResultDTO);

    /**
     * Partially updates a quizResult.
     *
     * @param quizResultDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuizResultDTO> partialUpdate(QuizResultDTO quizResultDTO);

    /**
     * Get all the quizResults.
     *
     * @return the list of entities.
     */
    List<QuizResultDTO> findAll();

    /**
     * Get the "id" quizResult.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuizResultDTO> findOne(Long id);

    /**
     * Delete the "id" quizResult.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
