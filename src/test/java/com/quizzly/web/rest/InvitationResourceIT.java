package com.quizzly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.quizzly.IntegrationTest;
import com.quizzly.domain.Invitation;
import com.quizzly.repository.InvitationRepository;
import com.quizzly.service.InvitationService;
import com.quizzly.service.dto.InvitationDTO;
import com.quizzly.service.mapper.InvitationMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link InvitationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InvitationResourceIT {

    private static final String DEFAULT_QUIZ_CODE = "AAAAAAAAAA";
    private static final String UPDATED_QUIZ_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_INVITED_BY = "AAAAAAAAAA";
    private static final String UPDATED_INVITED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/invitations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InvitationRepository invitationRepository;

    @Mock
    private InvitationRepository invitationRepositoryMock;

    @Autowired
    private InvitationMapper invitationMapper;

    @Mock
    private InvitationService invitationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvitationMockMvc;

    private Invitation invitation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invitation createEntity(EntityManager em) {
        Invitation invitation = new Invitation().quizCode(DEFAULT_QUIZ_CODE).invitedBy(DEFAULT_INVITED_BY);
        return invitation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invitation createUpdatedEntity(EntityManager em) {
        Invitation invitation = new Invitation().quizCode(UPDATED_QUIZ_CODE).invitedBy(UPDATED_INVITED_BY);
        return invitation;
    }

    @BeforeEach
    public void initTest() {
        invitation = createEntity(em);
    }

    @Test
    @Transactional
    void createInvitation() throws Exception {
        int databaseSizeBeforeCreate = invitationRepository.findAll().size();
        // Create the Invitation
        InvitationDTO invitationDTO = invitationMapper.toDto(invitation);
        restInvitationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invitationDTO)))
            .andExpect(status().isCreated());

        // Validate the Invitation in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeCreate + 1);
        Invitation testInvitation = invitationList.get(invitationList.size() - 1);
        assertThat(testInvitation.getQuizCode()).isEqualTo(DEFAULT_QUIZ_CODE);
        assertThat(testInvitation.getInvitedBy()).isEqualTo(DEFAULT_INVITED_BY);
    }

    @Test
    @Transactional
    void createInvitationWithExistingId() throws Exception {
        // Create the Invitation with an existing ID
        invitation.setId(1L);
        InvitationDTO invitationDTO = invitationMapper.toDto(invitation);

        int databaseSizeBeforeCreate = invitationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvitationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invitationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Invitation in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInvitations() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        // Get all the invitationList
        restInvitationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invitation.getId().intValue())))
            .andExpect(jsonPath("$.[*].quizCode").value(hasItem(DEFAULT_QUIZ_CODE)))
            .andExpect(jsonPath("$.[*].invitedBy").value(hasItem(DEFAULT_INVITED_BY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInvitationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(invitationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInvitationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(invitationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInvitationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(invitationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInvitationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(invitationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getInvitation() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        // Get the invitation
        restInvitationMockMvc
            .perform(get(ENTITY_API_URL_ID, invitation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invitation.getId().intValue()))
            .andExpect(jsonPath("$.quizCode").value(DEFAULT_QUIZ_CODE))
            .andExpect(jsonPath("$.invitedBy").value(DEFAULT_INVITED_BY));
    }

    @Test
    @Transactional
    void getNonExistingInvitation() throws Exception {
        // Get the invitation
        restInvitationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInvitation() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        int databaseSizeBeforeUpdate = invitationRepository.findAll().size();

        // Update the invitation
        Invitation updatedInvitation = invitationRepository.findById(invitation.getId()).get();
        // Disconnect from session so that the updates on updatedInvitation are not directly saved in db
        em.detach(updatedInvitation);
        updatedInvitation.quizCode(UPDATED_QUIZ_CODE).invitedBy(UPDATED_INVITED_BY);
        InvitationDTO invitationDTO = invitationMapper.toDto(updatedInvitation);

        restInvitationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invitationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invitationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Invitation in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeUpdate);
        Invitation testInvitation = invitationList.get(invitationList.size() - 1);
        assertThat(testInvitation.getQuizCode()).isEqualTo(UPDATED_QUIZ_CODE);
        assertThat(testInvitation.getInvitedBy()).isEqualTo(UPDATED_INVITED_BY);
    }

    @Test
    @Transactional
    void putNonExistingInvitation() throws Exception {
        int databaseSizeBeforeUpdate = invitationRepository.findAll().size();
        invitation.setId(count.incrementAndGet());

        // Create the Invitation
        InvitationDTO invitationDTO = invitationMapper.toDto(invitation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvitationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invitationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invitationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invitation in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvitation() throws Exception {
        int databaseSizeBeforeUpdate = invitationRepository.findAll().size();
        invitation.setId(count.incrementAndGet());

        // Create the Invitation
        InvitationDTO invitationDTO = invitationMapper.toDto(invitation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvitationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invitationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invitation in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvitation() throws Exception {
        int databaseSizeBeforeUpdate = invitationRepository.findAll().size();
        invitation.setId(count.incrementAndGet());

        // Create the Invitation
        InvitationDTO invitationDTO = invitationMapper.toDto(invitation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvitationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invitationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invitation in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvitationWithPatch() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        int databaseSizeBeforeUpdate = invitationRepository.findAll().size();

        // Update the invitation using partial update
        Invitation partialUpdatedInvitation = new Invitation();
        partialUpdatedInvitation.setId(invitation.getId());

        partialUpdatedInvitation.quizCode(UPDATED_QUIZ_CODE).invitedBy(UPDATED_INVITED_BY);

        restInvitationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvitation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvitation))
            )
            .andExpect(status().isOk());

        // Validate the Invitation in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeUpdate);
        Invitation testInvitation = invitationList.get(invitationList.size() - 1);
        assertThat(testInvitation.getQuizCode()).isEqualTo(UPDATED_QUIZ_CODE);
        assertThat(testInvitation.getInvitedBy()).isEqualTo(UPDATED_INVITED_BY);
    }

    @Test
    @Transactional
    void fullUpdateInvitationWithPatch() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        int databaseSizeBeforeUpdate = invitationRepository.findAll().size();

        // Update the invitation using partial update
        Invitation partialUpdatedInvitation = new Invitation();
        partialUpdatedInvitation.setId(invitation.getId());

        partialUpdatedInvitation.quizCode(UPDATED_QUIZ_CODE).invitedBy(UPDATED_INVITED_BY);

        restInvitationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvitation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvitation))
            )
            .andExpect(status().isOk());

        // Validate the Invitation in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeUpdate);
        Invitation testInvitation = invitationList.get(invitationList.size() - 1);
        assertThat(testInvitation.getQuizCode()).isEqualTo(UPDATED_QUIZ_CODE);
        assertThat(testInvitation.getInvitedBy()).isEqualTo(UPDATED_INVITED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingInvitation() throws Exception {
        int databaseSizeBeforeUpdate = invitationRepository.findAll().size();
        invitation.setId(count.incrementAndGet());

        // Create the Invitation
        InvitationDTO invitationDTO = invitationMapper.toDto(invitation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvitationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invitationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invitationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invitation in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvitation() throws Exception {
        int databaseSizeBeforeUpdate = invitationRepository.findAll().size();
        invitation.setId(count.incrementAndGet());

        // Create the Invitation
        InvitationDTO invitationDTO = invitationMapper.toDto(invitation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvitationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invitationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invitation in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvitation() throws Exception {
        int databaseSizeBeforeUpdate = invitationRepository.findAll().size();
        invitation.setId(count.incrementAndGet());

        // Create the Invitation
        InvitationDTO invitationDTO = invitationMapper.toDto(invitation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvitationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(invitationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invitation in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvitation() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        int databaseSizeBeforeDelete = invitationRepository.findAll().size();

        // Delete the invitation
        restInvitationMockMvc
            .perform(delete(ENTITY_API_URL_ID, invitation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
