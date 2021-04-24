package com.quizzly.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserAccount.
 */
@Entity
@Table(name = "user_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_rank")
    private Long rank;

    @Column(name = "quizes_taken")
    private Long quizesTaken;

    @Column(name = "quizzes_created")
    private Long quizzesCreated;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "participant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "question", "participant", "rezult" }, allowSetters = true)
    private Set<QuestionAnswer> questionAnswers = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "quizRezults", "questions", "owner" }, allowSetters = true)
    private Set<Quiz> quizzes = new HashSet<>();

    @OneToMany(mappedBy = "quizRezult")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "questionAnswers", "quiz", "quizRezult" }, allowSetters = true)
    private Set<QuizRezult> quizRezults = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserAccount id(Long id) {
        this.id = id;
        return this;
    }

    public Long getRank() {
        return this.rank;
    }

    public UserAccount rank(Long rank) {
        this.rank = rank;
        return this;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public Long getQuizesTaken() {
        return this.quizesTaken;
    }

    public UserAccount quizesTaken(Long quizesTaken) {
        this.quizesTaken = quizesTaken;
        return this;
    }

    public void setQuizesTaken(Long quizesTaken) {
        this.quizesTaken = quizesTaken;
    }

    public Long getQuizzesCreated() {
        return this.quizzesCreated;
    }

    public UserAccount quizzesCreated(Long quizzesCreated) {
        this.quizzesCreated = quizzesCreated;
        return this;
    }

    public void setQuizzesCreated(Long quizzesCreated) {
        this.quizzesCreated = quizzesCreated;
    }

    public User getUser() {
        return this.user;
    }

    public UserAccount user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<QuestionAnswer> getQuestionAnswers() {
        return this.questionAnswers;
    }

    public UserAccount questionAnswers(Set<QuestionAnswer> questionAnswers) {
        this.setQuestionAnswers(questionAnswers);
        return this;
    }

    public UserAccount addQuestionAnswer(QuestionAnswer questionAnswer) {
        this.questionAnswers.add(questionAnswer);
        questionAnswer.setParticipant(this);
        return this;
    }

    public UserAccount removeQuestionAnswer(QuestionAnswer questionAnswer) {
        this.questionAnswers.remove(questionAnswer);
        questionAnswer.setParticipant(null);
        return this;
    }

    public void setQuestionAnswers(Set<QuestionAnswer> questionAnswers) {
        if (this.questionAnswers != null) {
            this.questionAnswers.forEach(i -> i.setParticipant(null));
        }
        if (questionAnswers != null) {
            questionAnswers.forEach(i -> i.setParticipant(this));
        }
        this.questionAnswers = questionAnswers;
    }

    public Set<Quiz> getQuizzes() {
        return this.quizzes;
    }

    public UserAccount quizzes(Set<Quiz> quizzes) {
        this.setQuizzes(quizzes);
        return this;
    }

    public UserAccount addQuiz(Quiz quiz) {
        this.quizzes.add(quiz);
        quiz.setOwner(this);
        return this;
    }

    public UserAccount removeQuiz(Quiz quiz) {
        this.quizzes.remove(quiz);
        quiz.setOwner(null);
        return this;
    }

    public void setQuizzes(Set<Quiz> quizzes) {
        if (this.quizzes != null) {
            this.quizzes.forEach(i -> i.setOwner(null));
        }
        if (quizzes != null) {
            quizzes.forEach(i -> i.setOwner(this));
        }
        this.quizzes = quizzes;
    }

    public Set<QuizRezult> getQuizRezults() {
        return this.quizRezults;
    }

    public UserAccount quizRezults(Set<QuizRezult> quizRezults) {
        this.setQuizRezults(quizRezults);
        return this;
    }

    public UserAccount addQuizRezult(QuizRezult quizRezult) {
        this.quizRezults.add(quizRezult);
        quizRezult.setQuizRezult(this);
        return this;
    }

    public UserAccount removeQuizRezult(QuizRezult quizRezult) {
        this.quizRezults.remove(quizRezult);
        quizRezult.setQuizRezult(null);
        return this;
    }

    public void setQuizRezults(Set<QuizRezult> quizRezults) {
        if (this.quizRezults != null) {
            this.quizRezults.forEach(i -> i.setQuizRezult(null));
        }
        if (quizRezults != null) {
            quizRezults.forEach(i -> i.setQuizRezult(this));
        }
        this.quizRezults = quizRezults;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAccount)) {
            return false;
        }
        return id != null && id.equals(((UserAccount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAccount{" +
            "id=" + getId() +
            ", rank=" + getRank() +
            ", quizesTaken=" + getQuizesTaken() +
            ", quizzesCreated=" + getQuizzesCreated() +
            "}";
    }
}
