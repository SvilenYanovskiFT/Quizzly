package com.quizzly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.quizzly.IntegrationTest;
import com.quizzly.domain.QuizResult;
import com.quizzly.repository.QuizResultRepository;
import com.quizzly.service.dto.QuizResultDTO;
import com.quizzly.service.mapper.QuizResultMapper;
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
 * Integration tests for the {@link QuizResultResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuizResultResourceIT {

    private static final Long DEFAULT_SCORE = 1L;
    private static final Long UPDATED_SCORE = 2L;

    private static final Long DEFAULT_RANK = 1L;
    private static final Long UPDATED_RANK = 2L;

    private static final String ENTITY_API_URL = "/api/quiz-results";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuizResultRepository quizResultRepository;

    @Autowired
    private QuizResultMapper quizResultMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuizResultMockMvc;

    private QuizResult quizResult;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizResult createEntity(EntityManager em) {
        QuizResult quizResult = new QuizResult().score(DEFAULT_SCORE).rank(DEFAULT_RANK);
        return quizResult;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizResult createUpdatedEntity(EntityManager em) {
        QuizResult quizResult = new QuizResult().score(UPDATED_SCORE).rank(UPDATED_RANK);
        return quizResult;
    }

    @BeforeEach
    public void initTest() {
        quizResult = createEntity(em);
    }

    @Test
    @Transactional
    void createQuizResult() throws Exception {
        int databaseSizeBeforeCreate = quizResultRepository.findAll().size();
        // Create the QuizResult
        QuizResultDTO quizResultDTO = quizResultMapper.toDto(quizResult);
        restQuizResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizResultDTO)))
            .andExpect(status().isCreated());

        // Validate the QuizResult in the database
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeCreate + 1);
        QuizResult testQuizResult = quizResultList.get(quizResultList.size() - 1);
        assertThat(testQuizResult.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testQuizResult.getRank()).isEqualTo(DEFAULT_RANK);
    }

    @Test
    @Transactional
    void createQuizResultWithExistingId() throws Exception {
        // Create the QuizResult with an existing ID
        quizResult.setId(1L);
        QuizResultDTO quizResultDTO = quizResultMapper.toDto(quizResult);

        int databaseSizeBeforeCreate = quizResultRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuizResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizResultDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuizResult in the database
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuizResults() throws Exception {
        // Initialize the database
        quizResultRepository.saveAndFlush(quizResult);

        // Get all the quizResultList
        restQuizResultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quizResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE.intValue())))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK.intValue())));
    }

    @Test
    @Transactional
    void getQuizResult() throws Exception {
        // Initialize the database
        quizResultRepository.saveAndFlush(quizResult);

        // Get the quizResult
        restQuizResultMockMvc
            .perform(get(ENTITY_API_URL_ID, quizResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quizResult.getId().intValue()))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE.intValue()))
            .andExpect(jsonPath("$.rank").value(DEFAULT_RANK.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingQuizResult() throws Exception {
        // Get the quizResult
        restQuizResultMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuizResult() throws Exception {
        // Initialize the database
        quizResultRepository.saveAndFlush(quizResult);

        int databaseSizeBeforeUpdate = quizResultRepository.findAll().size();

        // Update the quizResult
        QuizResult updatedQuizResult = quizResultRepository.findById(quizResult.getId()).get();
        // Disconnect from session so that the updates on updatedQuizResult are not directly saved in db
        em.detach(updatedQuizResult);
        updatedQuizResult.score(UPDATED_SCORE).rank(UPDATED_RANK);
        QuizResultDTO quizResultDTO = quizResultMapper.toDto(updatedQuizResult);

        restQuizResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizResultDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizResultDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuizResult in the database
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeUpdate);
        QuizResult testQuizResult = quizResultList.get(quizResultList.size() - 1);
        assertThat(testQuizResult.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testQuizResult.getRank()).isEqualTo(UPDATED_RANK);
    }

    @Test
    @Transactional
    void putNonExistingQuizResult() throws Exception {
        int databaseSizeBeforeUpdate = quizResultRepository.findAll().size();
        quizResult.setId(count.incrementAndGet());

        // Create the QuizResult
        QuizResultDTO quizResultDTO = quizResultMapper.toDto(quizResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizResultDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizResult in the database
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuizResult() throws Exception {
        int databaseSizeBeforeUpdate = quizResultRepository.findAll().size();
        quizResult.setId(count.incrementAndGet());

        // Create the QuizResult
        QuizResultDTO quizResultDTO = quizResultMapper.toDto(quizResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizResult in the database
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuizResult() throws Exception {
        int databaseSizeBeforeUpdate = quizResultRepository.findAll().size();
        quizResult.setId(count.incrementAndGet());

        // Create the QuizResult
        QuizResultDTO quizResultDTO = quizResultMapper.toDto(quizResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizResultMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizResultDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuizResult in the database
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuizResultWithPatch() throws Exception {
        // Initialize the database
        quizResultRepository.saveAndFlush(quizResult);

        int databaseSizeBeforeUpdate = quizResultRepository.findAll().size();

        // Update the quizResult using partial update
        QuizResult partialUpdatedQuizResult = new QuizResult();
        partialUpdatedQuizResult.setId(quizResult.getId());

        partialUpdatedQuizResult.score(UPDATED_SCORE);

        restQuizResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuizResult))
            )
            .andExpect(status().isOk());

        // Validate the QuizResult in the database
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeUpdate);
        QuizResult testQuizResult = quizResultList.get(quizResultList.size() - 1);
        assertThat(testQuizResult.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testQuizResult.getRank()).isEqualTo(DEFAULT_RANK);
    }

    @Test
    @Transactional
    void fullUpdateQuizResultWithPatch() throws Exception {
        // Initialize the database
        quizResultRepository.saveAndFlush(quizResult);

        int databaseSizeBeforeUpdate = quizResultRepository.findAll().size();

        // Update the quizResult using partial update
        QuizResult partialUpdatedQuizResult = new QuizResult();
        partialUpdatedQuizResult.setId(quizResult.getId());

        partialUpdatedQuizResult.score(UPDATED_SCORE).rank(UPDATED_RANK);

        restQuizResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuizResult))
            )
            .andExpect(status().isOk());

        // Validate the QuizResult in the database
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeUpdate);
        QuizResult testQuizResult = quizResultList.get(quizResultList.size() - 1);
        assertThat(testQuizResult.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testQuizResult.getRank()).isEqualTo(UPDATED_RANK);
    }

    @Test
    @Transactional
    void patchNonExistingQuizResult() throws Exception {
        int databaseSizeBeforeUpdate = quizResultRepository.findAll().size();
        quizResult.setId(count.incrementAndGet());

        // Create the QuizResult
        QuizResultDTO quizResultDTO = quizResultMapper.toDto(quizResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quizResultDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quizResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizResult in the database
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuizResult() throws Exception {
        int databaseSizeBeforeUpdate = quizResultRepository.findAll().size();
        quizResult.setId(count.incrementAndGet());

        // Create the QuizResult
        QuizResultDTO quizResultDTO = quizResultMapper.toDto(quizResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quizResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizResult in the database
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuizResult() throws Exception {
        int databaseSizeBeforeUpdate = quizResultRepository.findAll().size();
        quizResult.setId(count.incrementAndGet());

        // Create the QuizResult
        QuizResultDTO quizResultDTO = quizResultMapper.toDto(quizResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizResultMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(quizResultDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuizResult in the database
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuizResult() throws Exception {
        // Initialize the database
        quizResultRepository.saveAndFlush(quizResult);

        int databaseSizeBeforeDelete = quizResultRepository.findAll().size();

        // Delete the quizResult
        restQuizResultMockMvc
            .perform(delete(ENTITY_API_URL_ID, quizResult.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
