package com.quizzly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.quizzly.IntegrationTest;
import com.quizzly.domain.QuizRezult;
import com.quizzly.repository.QuizRezultRepository;
import com.quizzly.service.dto.QuizRezultDTO;
import com.quizzly.service.mapper.QuizRezultMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link QuizRezultResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuizRezultResourceIT {

    private static final Long DEFAULT_SCORE = 1L;
    private static final Long UPDATED_SCORE = 2L;

    private static final Long DEFAULT_RANK = 1L;
    private static final Long UPDATED_RANK = 2L;

    private static final String ENTITY_API_URL = "/api/quiz-rezults";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuizRezultRepository quizRezultRepository;

    @Autowired
    private QuizRezultMapper quizRezultMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuizRezultMockMvc;

    private QuizRezult quizRezult;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizRezult createEntity(EntityManager em) {
        QuizRezult quizRezult = new QuizRezult().score(DEFAULT_SCORE).rank(DEFAULT_RANK);
        return quizRezult;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizRezult createUpdatedEntity(EntityManager em) {
        QuizRezult quizRezult = new QuizRezult().score(UPDATED_SCORE).rank(UPDATED_RANK);
        return quizRezult;
    }

    @BeforeEach
    public void initTest() {
        quizRezult = createEntity(em);
    }

    @Test
    @Transactional
    void createQuizRezult() throws Exception {
        int databaseSizeBeforeCreate = quizRezultRepository.findAll().size();
        // Create the QuizRezult
        QuizRezultDTO quizRezultDTO = quizRezultMapper.toDto(quizRezult);
        restQuizRezultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizRezultDTO)))
            .andExpect(status().isCreated());

        // Validate the QuizRezult in the database
        List<QuizRezult> quizRezultList = quizRezultRepository.findAll();
        assertThat(quizRezultList).hasSize(databaseSizeBeforeCreate + 1);
        QuizRezult testQuizRezult = quizRezultList.get(quizRezultList.size() - 1);
        assertThat(testQuizRezult.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testQuizRezult.getRank()).isEqualTo(DEFAULT_RANK);
    }

    @Test
    @Transactional
    void createQuizRezultWithExistingId() throws Exception {
        // Create the QuizRezult with an existing ID
        quizRezult.setId(1L);
        QuizRezultDTO quizRezultDTO = quizRezultMapper.toDto(quizRezult);

        int databaseSizeBeforeCreate = quizRezultRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuizRezultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizRezultDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuizRezult in the database
        List<QuizRezult> quizRezultList = quizRezultRepository.findAll();
        assertThat(quizRezultList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuizRezults() throws Exception {
        // Initialize the database
        quizRezultRepository.saveAndFlush(quizRezult);

        // Get all the quizRezultList
        restQuizRezultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quizRezult.getId().intValue())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE.intValue())))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK.intValue())));
    }

    @Test
    @Transactional
    void getQuizRezult() throws Exception {
        // Initialize the database
        quizRezultRepository.saveAndFlush(quizRezult);

        // Get the quizRezult
        restQuizRezultMockMvc
            .perform(get(ENTITY_API_URL_ID, quizRezult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quizRezult.getId().intValue()))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE.intValue()))
            .andExpect(jsonPath("$.rank").value(DEFAULT_RANK.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingQuizRezult() throws Exception {
        // Get the quizRezult
        restQuizRezultMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuizRezult() throws Exception {
        // Initialize the database
        quizRezultRepository.saveAndFlush(quizRezult);

        int databaseSizeBeforeUpdate = quizRezultRepository.findAll().size();

        // Update the quizRezult
        QuizRezult updatedQuizRezult = quizRezultRepository.findById(quizRezult.getId()).get();
        // Disconnect from session so that the updates on updatedQuizRezult are not directly saved in db
        em.detach(updatedQuizRezult);
        updatedQuizRezult.score(UPDATED_SCORE).rank(UPDATED_RANK);
        QuizRezultDTO quizRezultDTO = quizRezultMapper.toDto(updatedQuizRezult);

        restQuizRezultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizRezultDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizRezultDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuizRezult in the database
        List<QuizRezult> quizRezultList = quizRezultRepository.findAll();
        assertThat(quizRezultList).hasSize(databaseSizeBeforeUpdate);
        QuizRezult testQuizRezult = quizRezultList.get(quizRezultList.size() - 1);
        assertThat(testQuizRezult.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testQuizRezult.getRank()).isEqualTo(UPDATED_RANK);
    }

    @Test
    @Transactional
    void putNonExistingQuizRezult() throws Exception {
        int databaseSizeBeforeUpdate = quizRezultRepository.findAll().size();
        quizRezult.setId(count.incrementAndGet());

        // Create the QuizRezult
        QuizRezultDTO quizRezultDTO = quizRezultMapper.toDto(quizRezult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizRezultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizRezultDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizRezultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizRezult in the database
        List<QuizRezult> quizRezultList = quizRezultRepository.findAll();
        assertThat(quizRezultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuizRezult() throws Exception {
        int databaseSizeBeforeUpdate = quizRezultRepository.findAll().size();
        quizRezult.setId(count.incrementAndGet());

        // Create the QuizRezult
        QuizRezultDTO quizRezultDTO = quizRezultMapper.toDto(quizRezult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizRezultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizRezultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizRezult in the database
        List<QuizRezult> quizRezultList = quizRezultRepository.findAll();
        assertThat(quizRezultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuizRezult() throws Exception {
        int databaseSizeBeforeUpdate = quizRezultRepository.findAll().size();
        quizRezult.setId(count.incrementAndGet());

        // Create the QuizRezult
        QuizRezultDTO quizRezultDTO = quizRezultMapper.toDto(quizRezult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizRezultMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizRezultDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuizRezult in the database
        List<QuizRezult> quizRezultList = quizRezultRepository.findAll();
        assertThat(quizRezultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuizRezultWithPatch() throws Exception {
        // Initialize the database
        quizRezultRepository.saveAndFlush(quizRezult);

        int databaseSizeBeforeUpdate = quizRezultRepository.findAll().size();

        // Update the quizRezult using partial update
        QuizRezult partialUpdatedQuizRezult = new QuizRezult();
        partialUpdatedQuizRezult.setId(quizRezult.getId());

        restQuizRezultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizRezult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuizRezult))
            )
            .andExpect(status().isOk());

        // Validate the QuizRezult in the database
        List<QuizRezult> quizRezultList = quizRezultRepository.findAll();
        assertThat(quizRezultList).hasSize(databaseSizeBeforeUpdate);
        QuizRezult testQuizRezult = quizRezultList.get(quizRezultList.size() - 1);
        assertThat(testQuizRezult.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testQuizRezult.getRank()).isEqualTo(DEFAULT_RANK);
    }

    @Test
    @Transactional
    void fullUpdateQuizRezultWithPatch() throws Exception {
        // Initialize the database
        quizRezultRepository.saveAndFlush(quizRezult);

        int databaseSizeBeforeUpdate = quizRezultRepository.findAll().size();

        // Update the quizRezult using partial update
        QuizRezult partialUpdatedQuizRezult = new QuizRezult();
        partialUpdatedQuizRezult.setId(quizRezult.getId());

        partialUpdatedQuizRezult.score(UPDATED_SCORE).rank(UPDATED_RANK);

        restQuizRezultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizRezult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuizRezult))
            )
            .andExpect(status().isOk());

        // Validate the QuizRezult in the database
        List<QuizRezult> quizRezultList = quizRezultRepository.findAll();
        assertThat(quizRezultList).hasSize(databaseSizeBeforeUpdate);
        QuizRezult testQuizRezult = quizRezultList.get(quizRezultList.size() - 1);
        assertThat(testQuizRezult.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testQuizRezult.getRank()).isEqualTo(UPDATED_RANK);
    }

    @Test
    @Transactional
    void patchNonExistingQuizRezult() throws Exception {
        int databaseSizeBeforeUpdate = quizRezultRepository.findAll().size();
        quizRezult.setId(count.incrementAndGet());

        // Create the QuizRezult
        QuizRezultDTO quizRezultDTO = quizRezultMapper.toDto(quizRezult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizRezultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quizRezultDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quizRezultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizRezult in the database
        List<QuizRezult> quizRezultList = quizRezultRepository.findAll();
        assertThat(quizRezultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuizRezult() throws Exception {
        int databaseSizeBeforeUpdate = quizRezultRepository.findAll().size();
        quizRezult.setId(count.incrementAndGet());

        // Create the QuizRezult
        QuizRezultDTO quizRezultDTO = quizRezultMapper.toDto(quizRezult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizRezultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quizRezultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizRezult in the database
        List<QuizRezult> quizRezultList = quizRezultRepository.findAll();
        assertThat(quizRezultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuizRezult() throws Exception {
        int databaseSizeBeforeUpdate = quizRezultRepository.findAll().size();
        quizRezult.setId(count.incrementAndGet());

        // Create the QuizRezult
        QuizRezultDTO quizRezultDTO = quizRezultMapper.toDto(quizRezult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizRezultMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(quizRezultDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuizRezult in the database
        List<QuizRezult> quizRezultList = quizRezultRepository.findAll();
        assertThat(quizRezultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuizRezult() throws Exception {
        // Initialize the database
        quizRezultRepository.saveAndFlush(quizRezult);

        int databaseSizeBeforeDelete = quizRezultRepository.findAll().size();

        // Delete the quizRezult
        restQuizRezultMockMvc
            .perform(delete(ENTITY_API_URL_ID, quizRezult.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuizRezult> quizRezultList = quizRezultRepository.findAll();
        assertThat(quizRezultList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
