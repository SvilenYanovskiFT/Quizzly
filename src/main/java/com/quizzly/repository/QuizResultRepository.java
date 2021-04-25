package com.quizzly.repository;

import com.quizzly.domain.QuizResult;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the QuizResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {}
