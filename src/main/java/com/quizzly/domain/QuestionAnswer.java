package com.quizzly.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quizzly.domain.enumeration.AnswerCode;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QuestionAnswer.
 */
@Entity
@Table(name = "question_answer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QuestionAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_taken")
    private Long timeTaken;

    @Column(name = "success")
    private Boolean success;

    @Enumerated(EnumType.STRING)
    @Column(name = "answer")
    private AnswerCode answer;

    @ManyToOne
    @JsonIgnoreProperties(value = { "questionAnswers", "questionCategory", "quizzes" }, allowSetters = true)
    private Question question;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "questionAnswers", "quizzes", "quizResults" }, allowSetters = true)
    private UserAccount participant;

    @ManyToOne
    @JsonIgnoreProperties(value = { "questionAnswers", "quiz", "quizResult" }, allowSetters = true)
    private QuizResult rezult;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuestionAnswer id(Long id) {
        this.id = id;
        return this;
    }

    public Long getTimeTaken() {
        return this.timeTaken;
    }

    public QuestionAnswer timeTaken(Long timeTaken) {
        this.timeTaken = timeTaken;
        return this;
    }

    public void setTimeTaken(Long timeTaken) {
        this.timeTaken = timeTaken;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public QuestionAnswer success(Boolean success) {
        this.success = success;
        return this;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public AnswerCode getAnswer() {
        return this.answer;
    }

    public QuestionAnswer answer(AnswerCode answer) {
        this.answer = answer;
        return this;
    }

    public void setAnswer(AnswerCode answer) {
        this.answer = answer;
    }

    public Question getQuestion() {
        return this.question;
    }

    public QuestionAnswer question(Question question) {
        this.setQuestion(question);
        return this;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public UserAccount getParticipant() {
        return this.participant;
    }

    public QuestionAnswer participant(UserAccount userAccount) {
        this.setParticipant(userAccount);
        return this;
    }

    public void setParticipant(UserAccount userAccount) {
        this.participant = userAccount;
    }

    public QuizResult getRezult() {
        return this.rezult;
    }

    public QuestionAnswer rezult(QuizResult quizResult) {
        this.setRezult(quizResult);
        return this;
    }

    public void setRezult(QuizResult quizResult) {
        this.rezult = quizResult;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionAnswer)) {
            return false;
        }
        return id != null && id.equals(((QuestionAnswer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionAnswer{" +
            "id=" + getId() +
            ", timeTaken=" + getTimeTaken() +
            ", success='" + getSuccess() + "'" +
            ", answer='" + getAnswer() + "'" +
            "}";
    }
}
