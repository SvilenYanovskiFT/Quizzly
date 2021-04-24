package com.quizzly.service;

import com.quizzly.service.dto.QuizRezultDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.quizzly.domain.QuizRezult}.
 */
public interface QuizRezultService {
    /**
     * Save a quizRezult.
     *
     * @param quizRezultDTO the entity to save.
     * @return the persisted entity.
     */
    QuizRezultDTO save(QuizRezultDTO quizRezultDTO);

    /**
     * Partially updates a quizRezult.
     *
     * @param quizRezultDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuizRezultDTO> partialUpdate(QuizRezultDTO quizRezultDTO);

    /**
     * Get all the quizRezults.
     *
     * @return the list of entities.
     */
    List<QuizRezultDTO> findAll();

    /**
     * Get the "id" quizRezult.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuizRezultDTO> findOne(Long id);

    /**
     * Delete the "id" quizRezult.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
