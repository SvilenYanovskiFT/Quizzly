package com.quizzly.repository;

import com.quizzly.domain.Quiz;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Quiz entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {}
