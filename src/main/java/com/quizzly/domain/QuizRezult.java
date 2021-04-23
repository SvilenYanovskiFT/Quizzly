package com.quizzly.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QuizRezult.
 */
@Entity
@Table(name = "quiz_rezult")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QuizRezult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "score")
    private Long score;

    @Column(name = "jhi_rank")
    private Long rank;

    @OneToMany(mappedBy = "rezult")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "question", "participant", "rezult" }, allowSetters = true)
    private Set<QuestionAnswer> questionAnswers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "questions", "quizRezults", "owner" }, allowSetters = true)
    private Quiz quiz;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "questionAnswers", "quizzes", "quizRezults" }, allowSetters = true)
    private UserAccount quizRezult;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuizRezult id(Long id) {
        this.id = id;
        return this;
    }

    public Long getScore() {
        return this.score;
    }

    public QuizRezult score(Long score) {
        this.score = score;
        return this;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Long getRank() {
        return this.rank;
    }

    public QuizRezult rank(Long rank) {
        this.rank = rank;
        return this;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public Set<QuestionAnswer> getQuestionAnswers() {
        return this.questionAnswers;
    }

    public QuizRezult questionAnswers(Set<QuestionAnswer> questionAnswers) {
        this.setQuestionAnswers(questionAnswers);
        return this;
    }

    public QuizRezult addQuestionAnswer(QuestionAnswer questionAnswer) {
        this.questionAnswers.add(questionAnswer);
        questionAnswer.setRezult(this);
        return this;
    }

    public QuizRezult removeQuestionAnswer(QuestionAnswer questionAnswer) {
        this.questionAnswers.remove(questionAnswer);
        questionAnswer.setRezult(null);
        return this;
    }

    public void setQuestionAnswers(Set<QuestionAnswer> questionAnswers) {
        if (this.questionAnswers != null) {
            this.questionAnswers.forEach(i -> i.setRezult(null));
        }
        if (questionAnswers != null) {
            questionAnswers.forEach(i -> i.setRezult(this));
        }
        this.questionAnswers = questionAnswers;
    }

    public Quiz getQuiz() {
        return this.quiz;
    }

    public QuizRezult quiz(Quiz quiz) {
        this.setQuiz(quiz);
        return this;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public UserAccount getQuizRezult() {
        return this.quizRezult;
    }

    public QuizRezult quizRezult(UserAccount userAccount) {
        this.setQuizRezult(userAccount);
        return this;
    }

    public void setQuizRezult(UserAccount userAccount) {
        this.quizRezult = userAccount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizRezult)) {
            return false;
        }
        return id != null && id.equals(((QuizRezult) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizRezult{" +
            "id=" + getId() +
            ", score=" + getScore() +
            ", rank=" + getRank() +
            "}";
    }
}
