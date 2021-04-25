package com.quizzly.repository;

import com.quizzly.domain.Invitation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Invitation entity.
 */
@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    @Query(
        value = "select distinct invitation from Invitation invitation left join fetch invitation.userAccounts",
        countQuery = "select count(distinct invitation) from Invitation invitation"
    )
    Page<Invitation> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct invitation from Invitation invitation left join fetch invitation.userAccounts")
    List<Invitation> findAllWithEagerRelationships();

    @Query("select invitation from Invitation invitation left join fetch invitation.userAccounts where invitation.id =:id")
    Optional<Invitation> findOneWithEagerRelationships(@Param("id") Long id);
}
