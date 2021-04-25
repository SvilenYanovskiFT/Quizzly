package com.quizzly.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QuestionCategory.
 */
@Entity
@Table(name = "question_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QuestionCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "questionCategory")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "questionAnswers", "questionCategory", "createdBy", "quizzes" }, allowSetters = true)
    private Set<Question> categories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuestionCategory id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public QuestionCategory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Question> getCategories() {
        return this.categories;
    }

    public QuestionCategory categories(Set<Question> questions) {
        this.setCategories(questions);
        return this;
    }

    public QuestionCategory addCategory(Question question) {
        this.categories.add(question);
        question.setQuestionCategory(this);
        return this;
    }

    public QuestionCategory removeCategory(Question question) {
        this.categories.remove(question);
        question.setQuestionCategory(null);
        return this;
    }

    public void setCategories(Set<Question> questions) {
        if (this.categories != null) {
            this.categories.forEach(i -> i.setQuestionCategory(null));
        }
        if (questions != null) {
            questions.forEach(i -> i.setQuestionCategory(this));
        }
        this.categories = questions;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionCategory)) {
            return false;
        }
        return id != null && id.equals(((QuestionCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
