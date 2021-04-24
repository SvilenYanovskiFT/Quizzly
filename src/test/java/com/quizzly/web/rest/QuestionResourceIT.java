package com.quizzly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.quizzly.IntegrationTest;
import com.quizzly.domain.Question;
import com.quizzly.domain.enumeration.AnswerCode;
import com.quizzly.repository.QuestionRepository;
import com.quizzly.service.dto.QuestionDTO;
import com.quizzly.service.mapper.QuestionMapper;
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
 * Integration tests for the {@link QuestionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionResourceIT {

    private static final Long DEFAULT_SORT_ORDER = 1L;
    private static final Long UPDATED_SORT_ORDER = 2L;

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_ANSWER_A = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER_A = "BBBBBBBBBB";

    private static final String DEFAULT_ANSWER_B = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER_B = "BBBBBBBBBB";

    private static final String DEFAULT_ANSWER_C = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER_C = "BBBBBBBBBB";

    private static final String DEFAULT_ANSWER_D = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER_D = "BBBBBBBBBB";

    private static final AnswerCode DEFAULT_CORRECT_ANSWER = AnswerCode.A;
    private static final AnswerCode UPDATED_CORRECT_ANSWER = AnswerCode.B;

    private static final Long DEFAULT_TIME_LIMIT = 1L;
    private static final Long UPDATED_TIME_LIMIT = 2L;

    private static final String ENTITY_API_URL = "/api/questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionMockMvc;

    private Question question;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createEntity(EntityManager em) {
        Question question = new Question()
            .sortOrder(DEFAULT_SORT_ORDER)
            .text(DEFAULT_TEXT)
            .image(DEFAULT_IMAGE)
            .answerA(DEFAULT_ANSWER_A)
            .answerB(DEFAULT_ANSWER_B)
            .answerC(DEFAULT_ANSWER_C)
            .answerD(DEFAULT_ANSWER_D)
            .correctAnswer(DEFAULT_CORRECT_ANSWER)
            .timeLimit(DEFAULT_TIME_LIMIT);
        return question;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createUpdatedEntity(EntityManager em) {
        Question question = new Question()
            .sortOrder(UPDATED_SORT_ORDER)
            .text(UPDATED_TEXT)
            .image(UPDATED_IMAGE)
            .answerA(UPDATED_ANSWER_A)
            .answerB(UPDATED_ANSWER_B)
            .answerC(UPDATED_ANSWER_C)
            .answerD(UPDATED_ANSWER_D)
            .correctAnswer(UPDATED_CORRECT_ANSWER)
            .timeLimit(UPDATED_TIME_LIMIT);
        return question;
    }

    @BeforeEach
    public void initTest() {
        question = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestion() throws Exception {
        int databaseSizeBeforeCreate = questionRepository.findAll().size();
        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);
        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionDTO)))
            .andExpect(status().isCreated());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeCreate + 1);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testQuestion.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testQuestion.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testQuestion.getAnswerA()).isEqualTo(DEFAULT_ANSWER_A);
        assertThat(testQuestion.getAnswerB()).isEqualTo(DEFAULT_ANSWER_B);
        assertThat(testQuestion.getAnswerC()).isEqualTo(DEFAULT_ANSWER_C);
        assertThat(testQuestion.getAnswerD()).isEqualTo(DEFAULT_ANSWER_D);
        assertThat(testQuestion.getCorrectAnswer()).isEqualTo(DEFAULT_CORRECT_ANSWER);
        assertThat(testQuestion.getTimeLimit()).isEqualTo(DEFAULT_TIME_LIMIT);
    }

    @Test
    @Transactional
    void createQuestionWithExistingId() throws Exception {
        // Create the Question with an existing ID
        question.setId(1L);
        QuestionDTO questionDTO = questionMapper.toDto(question);

        int databaseSizeBeforeCreate = questionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestions() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER.intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].answerA").value(hasItem(DEFAULT_ANSWER_A)))
            .andExpect(jsonPath("$.[*].answerB").value(hasItem(DEFAULT_ANSWER_B)))
            .andExpect(jsonPath("$.[*].answerC").value(hasItem(DEFAULT_ANSWER_C)))
            .andExpect(jsonPath("$.[*].answerD").value(hasItem(DEFAULT_ANSWER_D)))
            .andExpect(jsonPath("$.[*].correctAnswer").value(hasItem(DEFAULT_CORRECT_ANSWER.toString())))
            .andExpect(jsonPath("$.[*].timeLimit").value(hasItem(DEFAULT_TIME_LIMIT.intValue())));
    }

    @Test
    @Transactional
    void getQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get the question
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL_ID, question.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(question.getId().intValue()))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER.intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE))
            .andExpect(jsonPath("$.answerA").value(DEFAULT_ANSWER_A))
            .andExpect(jsonPath("$.answerB").value(DEFAULT_ANSWER_B))
            .andExpect(jsonPath("$.answerC").value(DEFAULT_ANSWER_C))
            .andExpect(jsonPath("$.answerD").value(DEFAULT_ANSWER_D))
            .andExpect(jsonPath("$.correctAnswer").value(DEFAULT_CORRECT_ANSWER.toString()))
            .andExpect(jsonPath("$.timeLimit").value(DEFAULT_TIME_LIMIT.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingQuestion() throws Exception {
        // Get the question
        restQuestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Update the question
        Question updatedQuestion = questionRepository.findById(question.getId()).get();
        // Disconnect from session so that the updates on updatedQuestion are not directly saved in db
        em.detach(updatedQuestion);
        updatedQuestion
            .sortOrder(UPDATED_SORT_ORDER)
            .text(UPDATED_TEXT)
            .image(UPDATED_IMAGE)
            .answerA(UPDATED_ANSWER_A)
            .answerB(UPDATED_ANSWER_B)
            .answerC(UPDATED_ANSWER_C)
            .answerD(UPDATED_ANSWER_D)
            .correctAnswer(UPDATED_CORRECT_ANSWER)
            .timeLimit(UPDATED_TIME_LIMIT);
        QuestionDTO questionDTO = questionMapper.toDto(updatedQuestion);

        restQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testQuestion.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testQuestion.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testQuestion.getAnswerA()).isEqualTo(UPDATED_ANSWER_A);
        assertThat(testQuestion.getAnswerB()).isEqualTo(UPDATED_ANSWER_B);
        assertThat(testQuestion.getAnswerC()).isEqualTo(UPDATED_ANSWER_C);
        assertThat(testQuestion.getAnswerD()).isEqualTo(UPDATED_ANSWER_D);
        assertThat(testQuestion.getCorrectAnswer()).isEqualTo(UPDATED_CORRECT_ANSWER);
        assertThat(testQuestion.getTimeLimit()).isEqualTo(UPDATED_TIME_LIMIT);
    }

    @Test
    @Transactional
    void putNonExistingQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        question.setId(count.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        question.setId(count.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        question.setId(count.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionWithPatch() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Update the question using partial update
        Question partialUpdatedQuestion = new Question();
        partialUpdatedQuestion.setId(question.getId());

        partialUpdatedQuestion
            .text(UPDATED_TEXT)
            .answerA(UPDATED_ANSWER_A)
            .answerB(UPDATED_ANSWER_B)
            .answerC(UPDATED_ANSWER_C)
            .correctAnswer(UPDATED_CORRECT_ANSWER)
            .timeLimit(UPDATED_TIME_LIMIT);

        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestion))
            )
            .andExpect(status().isOk());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testQuestion.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testQuestion.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testQuestion.getAnswerA()).isEqualTo(UPDATED_ANSWER_A);
        assertThat(testQuestion.getAnswerB()).isEqualTo(UPDATED_ANSWER_B);
        assertThat(testQuestion.getAnswerC()).isEqualTo(UPDATED_ANSWER_C);
        assertThat(testQuestion.getAnswerD()).isEqualTo(DEFAULT_ANSWER_D);
        assertThat(testQuestion.getCorrectAnswer()).isEqualTo(UPDATED_CORRECT_ANSWER);
        assertThat(testQuestion.getTimeLimit()).isEqualTo(UPDATED_TIME_LIMIT);
    }

    @Test
    @Transactional
    void fullUpdateQuestionWithPatch() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Update the question using partial update
        Question partialUpdatedQuestion = new Question();
        partialUpdatedQuestion.setId(question.getId());

        partialUpdatedQuestion
            .sortOrder(UPDATED_SORT_ORDER)
            .text(UPDATED_TEXT)
            .image(UPDATED_IMAGE)
            .answerA(UPDATED_ANSWER_A)
            .answerB(UPDATED_ANSWER_B)
            .answerC(UPDATED_ANSWER_C)
            .answerD(UPDATED_ANSWER_D)
            .correctAnswer(UPDATED_CORRECT_ANSWER)
            .timeLimit(UPDATED_TIME_LIMIT);

        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestion))
            )
            .andExpect(status().isOk());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testQuestion.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testQuestion.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testQuestion.getAnswerA()).isEqualTo(UPDATED_ANSWER_A);
        assertThat(testQuestion.getAnswerB()).isEqualTo(UPDATED_ANSWER_B);
        assertThat(testQuestion.getAnswerC()).isEqualTo(UPDATED_ANSWER_C);
        assertThat(testQuestion.getAnswerD()).isEqualTo(UPDATED_ANSWER_D);
        assertThat(testQuestion.getCorrectAnswer()).isEqualTo(UPDATED_CORRECT_ANSWER);
        assertThat(testQuestion.getTimeLimit()).isEqualTo(UPDATED_TIME_LIMIT);
    }

    @Test
    @Transactional
    void patchNonExistingQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        question.setId(count.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        question.setId(count.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        question.setId(count.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(questionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeDelete = questionRepository.findAll().size();

        // Delete the question
        restQuestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, question.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
