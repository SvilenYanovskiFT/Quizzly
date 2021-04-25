package com.quizzly.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quizzly.domain.enumeration.QuizType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Quiz.
 */
@Entity
@Table(name = "quiz")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Quiz implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "quiz_type")
    private QuizType quizType;

    @OneToMany(mappedBy = "quiz")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "questionAnswers", "quiz", "quizResult" }, allowSetters = true)
    private Set<QuizResult> quizResults = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_quiz__question",
        joinColumns = @JoinColumn(name = "quiz_id"),
        inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    @JsonIgnoreProperties(value = { "questionAnswers", "questionCategory", "createdBy", "quizzes" }, allowSetters = true)
    private Set<Question> questions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "questionAnswers", "quizzes", "questions", "quizResults", "invitations" }, allowSetters = true)
    private UserAccount owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Quiz id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Quiz name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public Quiz code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public QuizType getQuizType() {
        return this.quizType;
    }

    public Quiz quizType(QuizType quizType) {
        this.quizType = quizType;
        return this;
    }

    public void setQuizType(QuizType quizType) {
        this.quizType = quizType;
    }

    public Set<QuizResult> getQuizResults() {
        return this.quizResults;
    }

    public Quiz quizResults(Set<QuizResult> quizResults) {
        this.setQuizResults(quizResults);
        return this;
    }

    public Quiz addQuizResult(QuizResult quizResult) {
        this.quizResults.add(quizResult);
        quizResult.setQuiz(this);
        return this;
    }

    public Quiz removeQuizResult(QuizResult quizResult) {
        this.quizResults.remove(quizResult);
        quizResult.setQuiz(null);
        return this;
    }

    public void setQuizResults(Set<QuizResult> quizResults) {
        if (this.quizResults != null) {
            this.quizResults.forEach(i -> i.setQuiz(null));
        }
        if (quizResults != null) {
            quizResults.forEach(i -> i.setQuiz(this));
        }
        this.quizResults = quizResults;
    }

    public Set<Question> getQuestions() {
        return this.questions;
    }

    public Quiz questions(Set<Question> questions) {
        this.setQuestions(questions);
        return this;
    }

    public Quiz addQuestion(Question question) {
        this.questions.add(question);
        question.getQuizzes().add(this);
        return this;
    }

    public Quiz removeQuestion(Question question) {
        this.questions.remove(question);
        question.getQuizzes().remove(this);
        return this;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public UserAccount getOwner() {
        return this.owner;
    }

    public Quiz owner(UserAccount userAccount) {
        this.setOwner(userAccount);
        return this;
    }

    public void setOwner(UserAccount userAccount) {
        this.owner = userAccount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quiz)) {
            return false;
        }
        return id != null && id.equals(((Quiz) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Quiz{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", quizType='" + getQuizType() + "'" +
            "}";
    }
}
