package com.quizzly.repository;

import com.quizzly.domain.QuizRezult;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the QuizRezult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuizRezultRepository extends JpaRepository<QuizRezult, Long> {}
