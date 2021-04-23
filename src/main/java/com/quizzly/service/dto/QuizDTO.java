package com.quizzly.service.dto;

import com.quizzly.domain.enumeration.QuizType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.quizzly.domain.Quiz} entity.
 */
public class QuizDTO implements Serializable {

    private Long id;

    private String name;

    private String code;

    private QuizType quizType;

    private UserAccountDTO owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public QuizType getQuizType() {
        return quizType;
    }

    public void setQuizType(QuizType quizType) {
        this.quizType = quizType;
    }

    public UserAccountDTO getOwner() {
        return owner;
    }

    public void setOwner(UserAccountDTO owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizDTO)) {
            return false;
        }

        QuizDTO quizDTO = (QuizDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, quizDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", quizType='" + getQuizType() + "'" +
            ", owner=" + getOwner() +
            "}";
    }
}
