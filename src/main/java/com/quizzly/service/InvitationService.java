package com.quizzly.service;

import com.quizzly.service.dto.InvitationDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.quizzly.domain.Invitation}.
 */
public interface InvitationService {
    /**
     * Save a invitation.
     *
     * @param invitationDTO the entity to save.
     * @return the persisted entity.
     */
    InvitationDTO save(InvitationDTO invitationDTO);

    /**
     * Partially updates a invitation.
     *
     * @param invitationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InvitationDTO> partialUpdate(InvitationDTO invitationDTO);

    /**
     * Get all the invitations.
     *
     * @return the list of entities.
     */
    List<InvitationDTO> findAll();

    /**
     * Get all the invitations with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InvitationDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" invitation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InvitationDTO> findOne(Long id);

    /**
     * Delete the "id" invitation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
