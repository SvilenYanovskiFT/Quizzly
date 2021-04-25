package com.quizzly.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Invitation.
 */
@Entity
@Table(name = "invitation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Invitation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quiz_code")
    private String quizCode;

    @Column(name = "invited_by")
    private String invitedBy;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_invitation__user_account",
        joinColumns = @JoinColumn(name = "invitation_id"),
        inverseJoinColumns = @JoinColumn(name = "user_account_id")
    )
    @JsonIgnoreProperties(value = { "user", "questionAnswers", "quizzes", "questions", "quizResults", "invitations" }, allowSetters = true)
    private Set<UserAccount> userAccounts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Invitation id(Long id) {
        this.id = id;
        return this;
    }

    public String getQuizCode() {
        return this.quizCode;
    }

    public Invitation quizCode(String quizCode) {
        this.quizCode = quizCode;
        return this;
    }

    public void setQuizCode(String quizCode) {
        this.quizCode = quizCode;
    }

    public String getInvitedBy() {
        return this.invitedBy;
    }

    public Invitation invitedBy(String invitedBy) {
        this.invitedBy = invitedBy;
        return this;
    }

    public void setInvitedBy(String invitedBy) {
        this.invitedBy = invitedBy;
    }

    public Set<UserAccount> getUserAccounts() {
        return this.userAccounts;
    }

    public Invitation userAccounts(Set<UserAccount> userAccounts) {
        this.setUserAccounts(userAccounts);
        return this;
    }

    public Invitation addUserAccount(UserAccount userAccount) {
        this.userAccounts.add(userAccount);
        userAccount.getInvitations().add(this);
        return this;
    }

    public Invitation removeUserAccount(UserAccount userAccount) {
        this.userAccounts.remove(userAccount);
        userAccount.getInvitations().remove(this);
        return this;
    }

    public void setUserAccounts(Set<UserAccount> userAccounts) {
        this.userAccounts = userAccounts;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invitation)) {
            return false;
        }
        return id != null && id.equals(((Invitation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invitation{" +
            "id=" + getId() +
            ", quizCode='" + getQuizCode() + "'" +
            ", invitedBy='" + getInvitedBy() + "'" +
            "}";
    }
}
