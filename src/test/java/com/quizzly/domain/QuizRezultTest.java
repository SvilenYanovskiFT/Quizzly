package com.quizzly.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.quizzly.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuizRezultTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuizRezult.class);
        QuizRezult quizRezult1 = new QuizRezult();
        quizRezult1.setId(1L);
        QuizRezult quizRezult2 = new QuizRezult();
        quizRezult2.setId(quizRezult1.getId());
        assertThat(quizRezult1).isEqualTo(quizRezult2);
        quizRezult2.setId(2L);
        assertThat(quizRezult1).isNotEqualTo(quizRezult2);
        quizRezult1.setId(null);
        assertThat(quizRezult1).isNotEqualTo(quizRezult2);
    }
}
