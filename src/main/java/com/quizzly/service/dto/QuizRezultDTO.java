package com.quizzly.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.quizzly.domain.QuizRezult} entity.
 */
public class QuizRezultDTO implements Serializable {

    private Long id;

    private Long score;

    private Long rank;

    private QuizDTO quiz;

    private UserAccountDTO quizRezult;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public QuizDTO getQuiz() {
        return quiz;
    }

    public void setQuiz(QuizDTO quiz) {
        this.quiz = quiz;
    }

    public UserAccountDTO getQuizRezult() {
        return quizRezult;
    }

    public void setQuizRezult(UserAccountDTO quizRezult) {
        this.quizRezult = quizRezult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizRezultDTO)) {
            return false;
        }

        QuizRezultDTO quizRezultDTO = (QuizRezultDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, quizRezultDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizRezultDTO{" +
            "id=" + getId() +
            ", score=" + getScore() +
            ", rank=" + getRank() +
            ", quiz=" + getQuiz() +
            ", quizRezult=" + getQuizRezult() +
            "}";
    }
}
