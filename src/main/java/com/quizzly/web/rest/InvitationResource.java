package com.quizzly.web.rest;

import com.quizzly.repository.InvitationRepository;
import com.quizzly.service.InvitationService;
import com.quizzly.service.dto.InvitationDTO;
import com.quizzly.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.quizzly.domain.Invitation}.
 */
@RestController
@RequestMapping("/api")
public class InvitationResource {

    private final Logger log = LoggerFactory.getLogger(InvitationResource.class);

    private static final String ENTITY_NAME = "invitation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvitationService invitationService;

    private final InvitationRepository invitationRepository;

    public InvitationResource(InvitationService invitationService, InvitationRepository invitationRepository) {
        this.invitationService = invitationService;
        this.invitationRepository = invitationRepository;
    }

    /**
     * {@code POST  /invitations} : Create a new invitation.
     *
     * @param invitationDTO the invitationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invitationDTO, or with status {@code 400 (Bad Request)} if the invitation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/invitations")
    public ResponseEntity<InvitationDTO> createInvitation(@RequestBody InvitationDTO invitationDTO) throws URISyntaxException {
        log.debug("REST request to save Invitation : {}", invitationDTO);
        if (invitationDTO.getId() != null) {
            throw new BadRequestAlertException("A new invitation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvitationDTO result = invitationService.save(invitationDTO);
        return ResponseEntity
            .created(new URI("/api/invitations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /invitations/:id} : Updates an existing invitation.
     *
     * @param id the id of the invitationDTO to save.
     * @param invitationDTO the invitationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invitationDTO,
     * or with status {@code 400 (Bad Request)} if the invitationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invitationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/invitations/{id}")
    public ResponseEntity<InvitationDTO> updateInvitation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InvitationDTO invitationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Invitation : {}, {}", id, invitationDTO);
        if (invitationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invitationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invitationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InvitationDTO result = invitationService.save(invitationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, invitationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /invitations/:id} : Partial updates given fields of an existing invitation, field will ignore if it is null
     *
     * @param id the id of the invitationDTO to save.
     * @param invitationDTO the invitationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invitationDTO,
     * or with status {@code 400 (Bad Request)} if the invitationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the invitationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the invitationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/invitations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<InvitationDTO> partialUpdateInvitation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InvitationDTO invitationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Invitation partially : {}, {}", id, invitationDTO);
        if (invitationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invitationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invitationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvitationDTO> result = invitationService.partialUpdate(invitationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, invitationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /invitations} : get all the invitations.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invitations in body.
     */
    @GetMapping("/invitations")
    public List<InvitationDTO> getAllInvitations(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Invitations");
        return invitationService.findAll();
    }

    /**
     * {@code GET  /invitations/:id} : get the "id" invitation.
     *
     * @param id the id of the invitationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invitationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/invitations/{id}")
    public ResponseEntity<InvitationDTO> getInvitation(@PathVariable Long id) {
        log.debug("REST request to get Invitation : {}", id);
        Optional<InvitationDTO> invitationDTO = invitationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invitationDTO);
    }

    /**
     * {@code DELETE  /invitations/:id} : delete the "id" invitation.
     *
     * @param id the id of the invitationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/invitations/{id}")
    public ResponseEntity<Void> deleteInvitation(@PathVariable Long id) {
        log.debug("REST request to delete Invitation : {}", id);
        invitationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
