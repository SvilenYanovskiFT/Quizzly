package com.quizzly.repository;

import com.quizzly.domain.QuestionAnswer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the QuestionAnswer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long> {}
