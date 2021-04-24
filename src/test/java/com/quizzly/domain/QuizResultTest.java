package com.quizzly.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.quizzly.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuizResultTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuizResult.class);
        QuizResult quizResult1 = new QuizResult();
        quizResult1.setId(1L);
        QuizResult quizResult2 = new QuizResult();
        quizResult2.setId(quizResult1.getId());
        assertThat(quizResult1).isEqualTo(quizResult2);
        quizResult2.setId(2L);
        assertThat(quizResult1).isNotEqualTo(quizResult2);
        quizResult1.setId(null);
        assertThat(quizResult1).isNotEqualTo(quizResult2);
    }
}
