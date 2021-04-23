package com.quizzly.service.dto;

import com.quizzly.domain.enumeration.AnswerCode;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.quizzly.domain.QuestionAnswer} entity.
 */
public class QuestionAnswerDTO implements Serializable {

    private Long id;

    private Long timeTaken;

    private Boolean success;

    private AnswerCode answer;

    private QuestionDTO question;

    private UserAccountDTO participant;

    private QuizRezultDTO rezult;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Long timeTaken) {
        this.timeTaken = timeTaken;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public AnswerCode getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerCode answer) {
        this.answer = answer;
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    public UserAccountDTO getParticipant() {
        return participant;
    }

    public void setParticipant(UserAccountDTO participant) {
        this.participant = participant;
    }

    public QuizRezultDTO getRezult() {
        return rezult;
    }

    public void setRezult(QuizRezultDTO rezult) {
        this.rezult = rezult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionAnswerDTO)) {
            return false;
        }

        QuestionAnswerDTO questionAnswerDTO = (QuestionAnswerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionAnswerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionAnswerDTO{" +
            "id=" + getId() +
            ", timeTaken=" + getTimeTaken() +
            ", success='" + getSuccess() + "'" +
            ", answer='" + getAnswer() + "'" +
            ", question=" + getQuestion() +
            ", participant=" + getParticipant() +
            ", rezult=" + getRezult() +
            "}";
    }
}
