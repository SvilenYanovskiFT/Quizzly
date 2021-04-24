package com.quizzly.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.quizzly.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionCategory.class);
        QuestionCategory questionCategory1 = new QuestionCategory();
        questionCategory1.setId(1L);
        QuestionCategory questionCategory2 = new QuestionCategory();
        questionCategory2.setId(questionCategory1.getId());
        assertThat(questionCategory1).isEqualTo(questionCategory2);
        questionCategory2.setId(2L);
        assertThat(questionCategory1).isNotEqualTo(questionCategory2);
        questionCategory1.setId(null);
        assertThat(questionCategory1).isNotEqualTo(questionCategory2);
    }
}
