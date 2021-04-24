package com.quizzly.service.dto;

import com.quizzly.domain.enumeration.AnswerCode;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.quizzly.domain.Question} entity.
 */
public class QuestionDTO implements Serializable {

    private Long id;

    private Long sortOrder;

    private String text;

    private String image;

    private String answerA;

    private String answerB;

    private String answerC;

    private String answerD;

    private AnswerCode correctAnswer;

    private Long timeLimit;

    private QuestionCategoryDTO questionCategory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Long sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }

    public AnswerCode getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(AnswerCode correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public QuestionCategoryDTO getQuestionCategory() {
        return questionCategory;
    }

    public void setQuestionCategory(QuestionCategoryDTO questionCategory) {
        this.questionCategory = questionCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionDTO)) {
            return false;
        }

        QuestionDTO questionDTO = (QuestionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionDTO{" +
            "id=" + getId() +
            ", sortOrder=" + getSortOrder() +
            ", text='" + getText() + "'" +
            ", image='" + getImage() + "'" +
            ", answerA='" + getAnswerA() + "'" +
            ", answerB='" + getAnswerB() + "'" +
            ", answerC='" + getAnswerC() + "'" +
            ", answerD='" + getAnswerD() + "'" +
            ", correctAnswer='" + getCorrectAnswer() + "'" +
            ", timeLimit=" + getTimeLimit() +
            ", questionCategory=" + getQuestionCategory() +
            "}";
    }
}
