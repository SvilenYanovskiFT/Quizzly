package com.quizzly.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuizMapperTest {

    private QuizMapper quizMapper;

    @BeforeEach
    public void setUp() {
        quizMapper = new QuizMapperImpl();
    }
}
