package com.quizzly.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QuizResult.
 */
@Entity
@Table(name = "quiz_result")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QuizResult implements Serializable {

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
    @JsonIgnoreProperties(value = { "quizResults", "questions", "owner" }, allowSetters = true)
    private Quiz quiz;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "questionAnswers", "quizzes", "quizResults" }, allowSetters = true)
    private UserAccount quizResult;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuizResult id(Long id) {
        this.id = id;
        return this;
    }

    public Long getScore() {
        return this.score;
    }

    public QuizResult score(Long score) {
        this.score = score;
        return this;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Long getRank() {
        return this.rank;
    }

    public QuizResult rank(Long rank) {
        this.rank = rank;
        return this;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public Set<QuestionAnswer> getQuestionAnswers() {
        return this.questionAnswers;
    }

    public QuizResult questionAnswers(Set<QuestionAnswer> questionAnswers) {
        this.setQuestionAnswers(questionAnswers);
        return this;
    }

    public QuizResult addQuestionAnswer(QuestionAnswer questionAnswer) {
        this.questionAnswers.add(questionAnswer);
        questionAnswer.setRezult(this);
        return this;
    }

    public QuizResult removeQuestionAnswer(QuestionAnswer questionAnswer) {
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

    public QuizResult quiz(Quiz quiz) {
        this.setQuiz(quiz);
        return this;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public UserAccount getQuizResult() {
        return this.quizResult;
    }

    public QuizResult quizResult(UserAccount userAccount) {
        this.setQuizResult(userAccount);
        return this;
    }

    public void setQuizResult(UserAccount userAccount) {
        this.quizResult = userAccount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizResult)) {
            return false;
        }
        return id != null && id.equals(((QuizResult) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizResult{" +
            "id=" + getId() +
            ", score=" + getScore() +
            ", rank=" + getRank() +
            "}";
    }
}
