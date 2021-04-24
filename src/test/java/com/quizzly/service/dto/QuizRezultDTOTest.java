package com.quizzly.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.quizzly.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuizRezultDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuizRezultDTO.class);
        QuizRezultDTO quizRezultDTO1 = new QuizRezultDTO();
        quizRezultDTO1.setId(1L);
        QuizRezultDTO quizRezultDTO2 = new QuizRezultDTO();
        assertThat(quizRezultDTO1).isNotEqualTo(quizRezultDTO2);
        quizRezultDTO2.setId(quizRezultDTO1.getId());
        assertThat(quizRezultDTO1).isEqualTo(quizRezultDTO2);
        quizRezultDTO2.setId(2L);
        assertThat(quizRezultDTO1).isNotEqualTo(quizRezultDTO2);
        quizRezultDTO1.setId(null);
        assertThat(quizRezultDTO1).isNotEqualTo(quizRezultDTO2);
    }
}
