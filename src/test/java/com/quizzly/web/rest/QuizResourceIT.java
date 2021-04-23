package com.quizzly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.quizzly.IntegrationTest;
import com.quizzly.domain.Quiz;
import com.quizzly.domain.enumeration.QuizType;
import com.quizzly.repository.QuizRepository;
import com.quizzly.service.dto.QuizDTO;
import com.quizzly.service.mapper.QuizMapper;
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
 * Integration tests for the {@link QuizResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuizResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final QuizType DEFAULT_QUIZ_TYPE = QuizType.GROUP;
    private static final QuizType UPDATED_QUIZ_TYPE = QuizType.PERSONAL;

    private static final String ENTITY_API_URL = "/api/quizzes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizMapper quizMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuizMockMvc;

    private Quiz quiz;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quiz createEntity(EntityManager em) {
        Quiz quiz = new Quiz().name(DEFAULT_NAME).code(DEFAULT_CODE).quizType(DEFAULT_QUIZ_TYPE);
        return quiz;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quiz createUpdatedEntity(EntityManager em) {
        Quiz quiz = new Quiz().name(UPDATED_NAME).code(UPDATED_CODE).quizType(UPDATED_QUIZ_TYPE);
        return quiz;
    }

    @BeforeEach
    public void initTest() {
        quiz = createEntity(em);
    }

    @Test
    @Transactional
    void createQuiz() throws Exception {
        int databaseSizeBeforeCreate = quizRepository.findAll().size();
        // Create the Quiz
        QuizDTO quizDTO = quizMapper.toDto(quiz);
        restQuizMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizDTO)))
            .andExpect(status().isCreated());

        // Validate the Quiz in the database
        List<Quiz> quizList = quizRepository.findAll();
        assertThat(quizList).hasSize(databaseSizeBeforeCreate + 1);
        Quiz testQuiz = quizList.get(quizList.size() - 1);
        assertThat(testQuiz.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQuiz.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testQuiz.getQuizType()).isEqualTo(DEFAULT_QUIZ_TYPE);
    }

    @Test
    @Transactional
    void createQuizWithExistingId() throws Exception {
        // Create the Quiz with an existing ID
        quiz.setId(1L);
        QuizDTO quizDTO = quizMapper.toDto(quiz);

        int databaseSizeBeforeCreate = quizRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuizMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Quiz in the database
        List<Quiz> quizList = quizRepository.findAll();
        assertThat(quizList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuizzes() throws Exception {
        // Initialize the database
        quizRepository.saveAndFlush(quiz);

        // Get all the quizList
        restQuizMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quiz.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].quizType").value(hasItem(DEFAULT_QUIZ_TYPE.toString())));
    }

    @Test
    @Transactional
    void getQuiz() throws Exception {
        // Initialize the database
        quizRepository.saveAndFlush(quiz);

        // Get the quiz
        restQuizMockMvc
            .perform(get(ENTITY_API_URL_ID, quiz.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quiz.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.quizType").value(DEFAULT_QUIZ_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingQuiz() throws Exception {
        // Get the quiz
        restQuizMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuiz() throws Exception {
        // Initialize the database
        quizRepository.saveAndFlush(quiz);

        int databaseSizeBeforeUpdate = quizRepository.findAll().size();

        // Update the quiz
        Quiz updatedQuiz = quizRepository.findById(quiz.getId()).get();
        // Disconnect from session so that the updates on updatedQuiz are not directly saved in db
        em.detach(updatedQuiz);
        updatedQuiz.name(UPDATED_NAME).code(UPDATED_CODE).quizType(UPDATED_QUIZ_TYPE);
        QuizDTO quizDTO = quizMapper.toDto(updatedQuiz);

        restQuizMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizDTO))
            )
            .andExpect(status().isOk());

        // Validate the Quiz in the database
        List<Quiz> quizList = quizRepository.findAll();
        assertThat(quizList).hasSize(databaseSizeBeforeUpdate);
        Quiz testQuiz = quizList.get(quizList.size() - 1);
        assertThat(testQuiz.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQuiz.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testQuiz.getQuizType()).isEqualTo(UPDATED_QUIZ_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingQuiz() throws Exception {
        int databaseSizeBeforeUpdate = quizRepository.findAll().size();
        quiz.setId(count.incrementAndGet());

        // Create the Quiz
        QuizDTO quizDTO = quizMapper.toDto(quiz);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quiz in the database
        List<Quiz> quizList = quizRepository.findAll();
        assertThat(quizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuiz() throws Exception {
        int databaseSizeBeforeUpdate = quizRepository.findAll().size();
        quiz.setId(count.incrementAndGet());

        // Create the Quiz
        QuizDTO quizDTO = quizMapper.toDto(quiz);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quiz in the database
        List<Quiz> quizList = quizRepository.findAll();
        assertThat(quizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuiz() throws Exception {
        int databaseSizeBeforeUpdate = quizRepository.findAll().size();
        quiz.setId(count.incrementAndGet());

        // Create the Quiz
        QuizDTO quizDTO = quizMapper.toDto(quiz);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quiz in the database
        List<Quiz> quizList = quizRepository.findAll();
        assertThat(quizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuizWithPatch() throws Exception {
        // Initialize the database
        quizRepository.saveAndFlush(quiz);

        int databaseSizeBeforeUpdate = quizRepository.findAll().size();

        // Update the quiz using partial update
        Quiz partialUpdatedQuiz = new Quiz();
        partialUpdatedQuiz.setId(quiz.getId());

        partialUpdatedQuiz.name(UPDATED_NAME).quizType(UPDATED_QUIZ_TYPE);

        restQuizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuiz.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuiz))
            )
            .andExpect(status().isOk());

        // Validate the Quiz in the database
        List<Quiz> quizList = quizRepository.findAll();
        assertThat(quizList).hasSize(databaseSizeBeforeUpdate);
        Quiz testQuiz = quizList.get(quizList.size() - 1);
        assertThat(testQuiz.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQuiz.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testQuiz.getQuizType()).isEqualTo(UPDATED_QUIZ_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateQuizWithPatch() throws Exception {
        // Initialize the database
        quizRepository.saveAndFlush(quiz);

        int databaseSizeBeforeUpdate = quizRepository.findAll().size();

        // Update the quiz using partial update
        Quiz partialUpdatedQuiz = new Quiz();
        partialUpdatedQuiz.setId(quiz.getId());

        partialUpdatedQuiz.name(UPDATED_NAME).code(UPDATED_CODE).quizType(UPDATED_QUIZ_TYPE);

        restQuizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuiz.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuiz))
            )
            .andExpect(status().isOk());

        // Validate the Quiz in the database
        List<Quiz> quizList = quizRepository.findAll();
        assertThat(quizList).hasSize(databaseSizeBeforeUpdate);
        Quiz testQuiz = quizList.get(quizList.size() - 1);
        assertThat(testQuiz.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQuiz.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testQuiz.getQuizType()).isEqualTo(UPDATED_QUIZ_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingQuiz() throws Exception {
        int databaseSizeBeforeUpdate = quizRepository.findAll().size();
        quiz.setId(count.incrementAndGet());

        // Create the Quiz
        QuizDTO quizDTO = quizMapper.toDto(quiz);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quizDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quizDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quiz in the database
        List<Quiz> quizList = quizRepository.findAll();
        assertThat(quizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuiz() throws Exception {
        int databaseSizeBeforeUpdate = quizRepository.findAll().size();
        quiz.setId(count.incrementAndGet());

        // Create the Quiz
        QuizDTO quizDTO = quizMapper.toDto(quiz);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quizDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quiz in the database
        List<Quiz> quizList = quizRepository.findAll();
        assertThat(quizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuiz() throws Exception {
        int databaseSizeBeforeUpdate = quizRepository.findAll().size();
        quiz.setId(count.incrementAndGet());

        // Create the Quiz
        QuizDTO quizDTO = quizMapper.toDto(quiz);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(quizDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quiz in the database
        List<Quiz> quizList = quizRepository.findAll();
        assertThat(quizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuiz() throws Exception {
        // Initialize the database
        quizRepository.saveAndFlush(quiz);

        int databaseSizeBeforeDelete = quizRepository.findAll().size();

        // Delete the quiz
        restQuizMockMvc
            .perform(delete(ENTITY_API_URL_ID, quiz.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Quiz> quizList = quizRepository.findAll();
        assertThat(quizList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
