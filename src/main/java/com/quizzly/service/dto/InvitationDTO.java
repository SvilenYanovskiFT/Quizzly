package com.quizzly.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.quizzly.domain.Invitation} entity.
 */
public class InvitationDTO implements Serializable {

    private Long id;

    private String quizCode;

    private String invitedBy;

    private Set<UserAccountDTO> userAccounts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuizCode() {
        return quizCode;
    }

    public void setQuizCode(String quizCode) {
        this.quizCode = quizCode;
    }

    public String getInvitedBy() {
        return invitedBy;
    }

    public void setInvitedBy(String invitedBy) {
        this.invitedBy = invitedBy;
    }

    public Set<UserAccountDTO> getUserAccounts() {
        return userAccounts;
    }

    public void setUserAccounts(Set<UserAccountDTO> userAccounts) {
        this.userAccounts = userAccounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvitationDTO)) {
            return false;
        }

        InvitationDTO invitationDTO = (InvitationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, invitationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvitationDTO{" +
            "id=" + getId() +
            ", quizCode='" + getQuizCode() + "'" +
            ", invitedBy='" + getInvitedBy() + "'" +
            ", userAccounts=" + getUserAccounts() +
            "}";
    }
}
