package com.quizzly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.quizzly.IntegrationTest;
import com.quizzly.domain.QuestionAnswer;
import com.quizzly.domain.enumeration.AnswerCode;
import com.quizzly.repository.QuestionAnswerRepository;
import com.quizzly.service.dto.QuestionAnswerDTO;
import com.quizzly.service.mapper.QuestionAnswerMapper;
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
 * Integration tests for the {@link QuestionAnswerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionAnswerResourceIT {

    private static final Long DEFAULT_TIME_TAKEN = 1L;
    private static final Long UPDATED_TIME_TAKEN = 2L;

    private static final Boolean DEFAULT_SUCCESS = false;
    private static final Boolean UPDATED_SUCCESS = true;

    private static final AnswerCode DEFAULT_ANSWER = AnswerCode.A;
    private static final AnswerCode UPDATED_ANSWER = AnswerCode.B;

    private static final String ENTITY_API_URL = "/api/question-answers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestionAnswerRepository questionAnswerRepository;

    @Autowired
    private QuestionAnswerMapper questionAnswerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionAnswerMockMvc;

    private QuestionAnswer questionAnswer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionAnswer createEntity(EntityManager em) {
        QuestionAnswer questionAnswer = new QuestionAnswer().timeTaken(DEFAULT_TIME_TAKEN).success(DEFAULT_SUCCESS).answer(DEFAULT_ANSWER);
        return questionAnswer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionAnswer createUpdatedEntity(EntityManager em) {
        QuestionAnswer questionAnswer = new QuestionAnswer().timeTaken(UPDATED_TIME_TAKEN).success(UPDATED_SUCCESS).answer(UPDATED_ANSWER);
        return questionAnswer;
    }

    @BeforeEach
    public void initTest() {
        questionAnswer = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestionAnswer() throws Exception {
        int databaseSizeBeforeCreate = questionAnswerRepository.findAll().size();
        // Create the QuestionAnswer
        QuestionAnswerDTO questionAnswerDTO = questionAnswerMapper.toDto(questionAnswer);
        restQuestionAnswerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionAnswerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeCreate + 1);
        QuestionAnswer testQuestionAnswer = questionAnswerList.get(questionAnswerList.size() - 1);
        assertThat(testQuestionAnswer.getTimeTaken()).isEqualTo(DEFAULT_TIME_TAKEN);
        assertThat(testQuestionAnswer.getSuccess()).isEqualTo(DEFAULT_SUCCESS);
        assertThat(testQuestionAnswer.getAnswer()).isEqualTo(DEFAULT_ANSWER);
    }

    @Test
    @Transactional
    void createQuestionAnswerWithExistingId() throws Exception {
        // Create the QuestionAnswer with an existing ID
        questionAnswer.setId(1L);
        QuestionAnswerDTO questionAnswerDTO = questionAnswerMapper.toDto(questionAnswer);

        int databaseSizeBeforeCreate = questionAnswerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionAnswerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestionAnswers() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);

        // Get all the questionAnswerList
        restQuestionAnswerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionAnswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeTaken").value(hasItem(DEFAULT_TIME_TAKEN.intValue())))
            .andExpect(jsonPath("$.[*].success").value(hasItem(DEFAULT_SUCCESS.booleanValue())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER.toString())));
    }

    @Test
    @Transactional
    void getQuestionAnswer() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);

        // Get the questionAnswer
        restQuestionAnswerMockMvc
            .perform(get(ENTITY_API_URL_ID, questionAnswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionAnswer.getId().intValue()))
            .andExpect(jsonPath("$.timeTaken").value(DEFAULT_TIME_TAKEN.intValue()))
            .andExpect(jsonPath("$.success").value(DEFAULT_SUCCESS.booleanValue()))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER.toString()));
    }

    @Test
    @Transactional
    void getNonExistingQuestionAnswer() throws Exception {
        // Get the questionAnswer
        restQuestionAnswerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuestionAnswer() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);

        int databaseSizeBeforeUpdate = questionAnswerRepository.findAll().size();

        // Update the questionAnswer
        QuestionAnswer updatedQuestionAnswer = questionAnswerRepository.findById(questionAnswer.getId()).get();
        // Disconnect from session so that the updates on updatedQuestionAnswer are not directly saved in db
        em.detach(updatedQuestionAnswer);
        updatedQuestionAnswer.timeTaken(UPDATED_TIME_TAKEN).success(UPDATED_SUCCESS).answer(UPDATED_ANSWER);
        QuestionAnswerDTO questionAnswerDTO = questionAnswerMapper.toDto(updatedQuestionAnswer);

        restQuestionAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionAnswerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionAnswerDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeUpdate);
        QuestionAnswer testQuestionAnswer = questionAnswerList.get(questionAnswerList.size() - 1);
        assertThat(testQuestionAnswer.getTimeTaken()).isEqualTo(UPDATED_TIME_TAKEN);
        assertThat(testQuestionAnswer.getSuccess()).isEqualTo(UPDATED_SUCCESS);
        assertThat(testQuestionAnswer.getAnswer()).isEqualTo(UPDATED_ANSWER);
    }

    @Test
    @Transactional
    void putNonExistingQuestionAnswer() throws Exception {
        int databaseSizeBeforeUpdate = questionAnswerRepository.findAll().size();
        questionAnswer.setId(count.incrementAndGet());

        // Create the QuestionAnswer
        QuestionAnswerDTO questionAnswerDTO = questionAnswerMapper.toDto(questionAnswer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionAnswerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestionAnswer() throws Exception {
        int databaseSizeBeforeUpdate = questionAnswerRepository.findAll().size();
        questionAnswer.setId(count.incrementAndGet());

        // Create the QuestionAnswer
        QuestionAnswerDTO questionAnswerDTO = questionAnswerMapper.toDto(questionAnswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestionAnswer() throws Exception {
        int databaseSizeBeforeUpdate = questionAnswerRepository.findAll().size();
        questionAnswer.setId(count.incrementAndGet());

        // Create the QuestionAnswer
        QuestionAnswerDTO questionAnswerDTO = questionAnswerMapper.toDto(questionAnswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionAnswerMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionAnswerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionAnswerWithPatch() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);

        int databaseSizeBeforeUpdate = questionAnswerRepository.findAll().size();

        // Update the questionAnswer using partial update
        QuestionAnswer partialUpdatedQuestionAnswer = new QuestionAnswer();
        partialUpdatedQuestionAnswer.setId(questionAnswer.getId());

        partialUpdatedQuestionAnswer.success(UPDATED_SUCCESS).answer(UPDATED_ANSWER);

        restQuestionAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionAnswer))
            )
            .andExpect(status().isOk());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeUpdate);
        QuestionAnswer testQuestionAnswer = questionAnswerList.get(questionAnswerList.size() - 1);
        assertThat(testQuestionAnswer.getTimeTaken()).isEqualTo(DEFAULT_TIME_TAKEN);
        assertThat(testQuestionAnswer.getSuccess()).isEqualTo(UPDATED_SUCCESS);
        assertThat(testQuestionAnswer.getAnswer()).isEqualTo(UPDATED_ANSWER);
    }

    @Test
    @Transactional
    void fullUpdateQuestionAnswerWithPatch() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);

        int databaseSizeBeforeUpdate = questionAnswerRepository.findAll().size();

        // Update the questionAnswer using partial update
        QuestionAnswer partialUpdatedQuestionAnswer = new QuestionAnswer();
        partialUpdatedQuestionAnswer.setId(questionAnswer.getId());

        partialUpdatedQuestionAnswer.timeTaken(UPDATED_TIME_TAKEN).success(UPDATED_SUCCESS).answer(UPDATED_ANSWER);

        restQuestionAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionAnswer))
            )
            .andExpect(status().isOk());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeUpdate);
        QuestionAnswer testQuestionAnswer = questionAnswerList.get(questionAnswerList.size() - 1);
        assertThat(testQuestionAnswer.getTimeTaken()).isEqualTo(UPDATED_TIME_TAKEN);
        assertThat(testQuestionAnswer.getSuccess()).isEqualTo(UPDATED_SUCCESS);
        assertThat(testQuestionAnswer.getAnswer()).isEqualTo(UPDATED_ANSWER);
    }

    @Test
    @Transactional
    void patchNonExistingQuestionAnswer() throws Exception {
        int databaseSizeBeforeUpdate = questionAnswerRepository.findAll().size();
        questionAnswer.setId(count.incrementAndGet());

        // Create the QuestionAnswer
        QuestionAnswerDTO questionAnswerDTO = questionAnswerMapper.toDto(questionAnswer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionAnswerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestionAnswer() throws Exception {
        int databaseSizeBeforeUpdate = questionAnswerRepository.findAll().size();
        questionAnswer.setId(count.incrementAndGet());

        // Create the QuestionAnswer
        QuestionAnswerDTO questionAnswerDTO = questionAnswerMapper.toDto(questionAnswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestionAnswer() throws Exception {
        int databaseSizeBeforeUpdate = questionAnswerRepository.findAll().size();
        questionAnswer.setId(count.incrementAndGet());

        // Create the QuestionAnswer
        QuestionAnswerDTO questionAnswerDTO = questionAnswerMapper.toDto(questionAnswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionAnswerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestionAnswer() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);

        int databaseSizeBeforeDelete = questionAnswerRepository.findAll().size();

        // Delete the questionAnswer
        restQuestionAnswerMockMvc
            .perform(delete(ENTITY_API_URL_ID, questionAnswer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
