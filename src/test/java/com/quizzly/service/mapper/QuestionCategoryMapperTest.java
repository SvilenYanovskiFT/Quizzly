package com.quizzly.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionCategoryMapperTest {

    private QuestionCategoryMapper questionCategoryMapper;

    @BeforeEach
    public void setUp() {
        questionCategoryMapper = new QuestionCategoryMapperImpl();
    }
}
