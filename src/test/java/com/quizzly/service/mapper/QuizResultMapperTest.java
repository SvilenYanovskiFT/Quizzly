package com.quizzly.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuizResultMapperTest {

    private QuizResultMapper quizResultMapper;

    @BeforeEach
    public void setUp() {
        quizResultMapper = new QuizResultMapperImpl();
    }
}
