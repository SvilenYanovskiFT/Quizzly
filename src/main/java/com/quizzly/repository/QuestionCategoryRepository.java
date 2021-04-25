package com.quizzly.repository;

import com.quizzly.domain.QuestionCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the QuestionCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, Long> {}
