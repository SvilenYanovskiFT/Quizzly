package com.quizzly.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuizRezultMapperTest {

    private QuizRezultMapper quizRezultMapper;

    @BeforeEach
    public void setUp() {
        quizRezultMapper = new QuizRezultMapperImpl();
    }
}
