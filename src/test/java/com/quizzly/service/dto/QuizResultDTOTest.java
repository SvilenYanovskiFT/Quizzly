package com.quizzly.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.quizzly.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuizResultDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuizResultDTO.class);
        QuizResultDTO quizResultDTO1 = new QuizResultDTO();
        quizResultDTO1.setId(1L);
        QuizResultDTO quizResultDTO2 = new QuizResultDTO();
        assertThat(quizResultDTO1).isNotEqualTo(quizResultDTO2);
        quizResultDTO2.setId(quizResultDTO1.getId());
        assertThat(quizResultDTO1).isEqualTo(quizResultDTO2);
        quizResultDTO2.setId(2L);
        assertThat(quizResultDTO1).isNotEqualTo(quizResultDTO2);
        quizResultDTO1.setId(null);
        assertThat(quizResultDTO1).isNotEqualTo(quizResultDTO2);
    }
}
