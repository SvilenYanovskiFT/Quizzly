package com.quizzly.service;

import com.quizzly.service.dto.QuizDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.quizzly.domain.Quiz}.
 */
public interface QuizService {
    /**
     * Save a quiz.
     *
     * @param quizDTO the entity to save.
     * @return the persisted entity.
     */
    QuizDTO save(QuizDTO quizDTO);

    /**
     * Partially updates a quiz.
     *
     * @param quizDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuizDTO> partialUpdate(QuizDTO quizDTO);

    /**
     * Get all the quizzes.
     *
     * @return the list of entities.
     */
    List<QuizDTO> findAll();

    /**
     * Get all the quizzes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuizDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" quiz.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuizDTO> findOne(Long id);

    /**
     * Delete the "id" quiz.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
