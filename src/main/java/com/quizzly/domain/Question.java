package com.quizzly.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quizzly.domain.enumeration.AnswerCode;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sort_order")
    private Long sortOrder;

    @Column(name = "text")
    private String text;

    @Column(name = "image")
    private String image;

    @Column(name = "answer_a")
    private String answerA;

    @Column(name = "answer_b")
    private String answerB;

    @Column(name = "answer_c")
    private String answerC;

    @Column(name = "answer_d")
    private String answerD;

    @Enumerated(EnumType.STRING)
    @Column(name = "correct_answer")
    private AnswerCode correctAnswer;

    @Column(name = "time_limit")
    private Long timeLimit;

    @OneToMany(mappedBy = "question")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "question", "participant", "rezult" }, allowSetters = true)
    private Set<QuestionAnswer> questionAnswers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "categories" }, allowSetters = true)
    private QuestionCategory questionCategory;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "questionAnswers", "quizzes", "questions", "quizResults", "invitations" }, allowSetters = true)
    private UserAccount createdBy;

    @ManyToMany(mappedBy = "questions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "quizResults", "questions", "owner" }, allowSetters = true)
    private Set<Quiz> quizzes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Question id(Long id) {
        this.id = id;
        return this;
    }

    public Long getSortOrder() {
        return this.sortOrder;
    }

    public Question sortOrder(Long sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public void setSortOrder(Long sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getText() {
        return this.text;
    }

    public Question text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return this.image;
    }

    public Question image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAnswerA() {
        return this.answerA;
    }

    public Question answerA(String answerA) {
        this.answerA = answerA;
        return this;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerB() {
        return this.answerB;
    }

    public Question answerB(String answerB) {
        this.answerB = answerB;
        return this;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return this.answerC;
    }

    public Question answerC(String answerC) {
        this.answerC = answerC;
        return this;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public String getAnswerD() {
        return this.answerD;
    }

    public Question answerD(String answerD) {
        this.answerD = answerD;
        return this;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }

    public AnswerCode getCorrectAnswer() {
        return this.correctAnswer;
    }

    public Question correctAnswer(AnswerCode correctAnswer) {
        this.correctAnswer = correctAnswer;
        return this;
    }

    public void setCorrectAnswer(AnswerCode correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Long getTimeLimit() {
        return this.timeLimit;
    }

    public Question timeLimit(Long timeLimit) {
        this.timeLimit = timeLimit;
        return this;
    }

    public void setTimeLimit(Long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Set<QuestionAnswer> getQuestionAnswers() {
        return this.questionAnswers;
    }

    public Question questionAnswers(Set<QuestionAnswer> questionAnswers) {
        this.setQuestionAnswers(questionAnswers);
        return this;
    }

    public Question addQuestionAnswer(QuestionAnswer questionAnswer) {
        this.questionAnswers.add(questionAnswer);
        questionAnswer.setQuestion(this);
        return this;
    }

    public Question removeQuestionAnswer(QuestionAnswer questionAnswer) {
        this.questionAnswers.remove(questionAnswer);
        questionAnswer.setQuestion(null);
        return this;
    }

    public void setQuestionAnswers(Set<QuestionAnswer> questionAnswers) {
        if (this.questionAnswers != null) {
            this.questionAnswers.forEach(i -> i.setQuestion(null));
        }
        if (questionAnswers != null) {
            questionAnswers.forEach(i -> i.setQuestion(this));
        }
        this.questionAnswers = questionAnswers;
    }

    public QuestionCategory getQuestionCategory() {
        return this.questionCategory;
    }

    public Question questionCategory(QuestionCategory questionCategory) {
        this.setQuestionCategory(questionCategory);
        return this;
    }

    public void setQuestionCategory(QuestionCategory questionCategory) {
        this.questionCategory = questionCategory;
    }

    public UserAccount getCreatedBy() {
        return this.createdBy;
    }

    public Question createdBy(UserAccount userAccount) {
        this.setCreatedBy(userAccount);
        return this;
    }

    public void setCreatedBy(UserAccount userAccount) {
        this.createdBy = userAccount;
    }

    public Set<Quiz> getQuizzes() {
        return this.quizzes;
    }

    public Question quizzes(Set<Quiz> quizzes) {
        this.setQuizzes(quizzes);
        return this;
    }

    public Question addQuiz(Quiz quiz) {
        this.quizzes.add(quiz);
        quiz.getQuestions().add(this);
        return this;
    }

    public Question removeQuiz(Quiz quiz) {
        this.quizzes.remove(quiz);
        quiz.getQuestions().remove(this);
        return this;
    }

    public void setQuizzes(Set<Quiz> quizzes) {
        if (this.quizzes != null) {
            this.quizzes.forEach(i -> i.removeQuestion(this));
        }
        if (quizzes != null) {
            quizzes.forEach(i -> i.addQuestion(this));
        }
        this.quizzes = quizzes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return id != null && id.equals(((Question) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Question{" +
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
            "}";
    }
}
