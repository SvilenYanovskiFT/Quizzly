package com.quizzly.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.quizzly.domain.UserAccount} entity.
 */
public class UserAccountDTO implements Serializable {

    private Long id;

    private Long rank;

    private Long quizesTaken;

    private Long quizzesCreated;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public Long getQuizesTaken() {
        return quizesTaken;
    }

    public void setQuizesTaken(Long quizesTaken) {
        this.quizesTaken = quizesTaken;
    }

    public Long getQuizzesCreated() {
        return quizzesCreated;
    }

    public void setQuizzesCreated(Long quizzesCreated) {
        this.quizzesCreated = quizzesCreated;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAccountDTO)) {
            return false;
        }

        UserAccountDTO userAccountDTO = (UserAccountDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userAccountDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAccountDTO{" +
            "id=" + getId() +
            ", rank=" + getRank() +
            ", quizesTaken=" + getQuizesTaken() +
            ", quizzesCreated=" + getQuizzesCreated() +
            ", user=" + getUser() +
            "}";
    }
}
