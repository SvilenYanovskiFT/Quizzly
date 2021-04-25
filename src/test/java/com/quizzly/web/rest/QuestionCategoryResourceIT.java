package com.quizzly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.quizzly.IntegrationTest;
import com.quizzly.domain.QuestionCategory;
import com.quizzly.repository.QuestionCategoryRepository;
import com.quizzly.service.dto.QuestionCategoryDTO;
import com.quizzly.service.mapper.QuestionCategoryMapper;
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
 * Integration tests for the {@link QuestionCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/question-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestionCategoryRepository questionCategoryRepository;

    @Autowired
    private QuestionCategoryMapper questionCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionCategoryMockMvc;

    private QuestionCategory questionCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionCategory createEntity(EntityManager em) {
        QuestionCategory questionCategory = new QuestionCategory().name(DEFAULT_NAME);
        return questionCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionCategory createUpdatedEntity(EntityManager em) {
        QuestionCategory questionCategory = new QuestionCategory().name(UPDATED_NAME);
        return questionCategory;
    }

    @BeforeEach
    public void initTest() {
        questionCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestionCategory() throws Exception {
        int databaseSizeBeforeCreate = questionCategoryRepository.findAll().size();
        // Create the QuestionCategory
        QuestionCategoryDTO questionCategoryDTO = questionCategoryMapper.toDto(questionCategory);
        restQuestionCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the QuestionCategory in the database
        List<QuestionCategory> questionCategoryList = questionCategoryRepository.findAll();
        assertThat(questionCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        QuestionCategory testQuestionCategory = questionCategoryList.get(questionCategoryList.size() - 1);
        assertThat(testQuestionCategory.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createQuestionCategoryWithExistingId() throws Exception {
        // Create the QuestionCategory with an existing ID
        questionCategory.setId(1L);
        QuestionCategoryDTO questionCategoryDTO = questionCategoryMapper.toDto(questionCategory);

        int databaseSizeBeforeCreate = questionCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionCategory in the database
        List<QuestionCategory> questionCategoryList = questionCategoryRepository.findAll();
        assertThat(questionCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestionCategories() throws Exception {
        // Initialize the database
        questionCategoryRepository.saveAndFlush(questionCategory);

        // Get all the questionCategoryList
        restQuestionCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getQuestionCategory() throws Exception {
        // Initialize the database
        questionCategoryRepository.saveAndFlush(questionCategory);

        // Get the questionCategory
        restQuestionCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, questionCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingQuestionCategory() throws Exception {
        // Get the questionCategory
        restQuestionCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuestionCategory() throws Exception {
        // Initialize the database
        questionCategoryRepository.saveAndFlush(questionCategory);

        int databaseSizeBeforeUpdate = questionCategoryRepository.findAll().size();

        // Update the questionCategory
        QuestionCategory updatedQuestionCategory = questionCategoryRepository.findById(questionCategory.getId()).get();
        // Disconnect from session so that the updates on updatedQuestionCategory are not directly saved in db
        em.detach(updatedQuestionCategory);
        updatedQuestionCategory.name(UPDATED_NAME);
        QuestionCategoryDTO questionCategoryDTO = questionCategoryMapper.toDto(updatedQuestionCategory);

        restQuestionCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuestionCategory in the database
        List<QuestionCategory> questionCategoryList = questionCategoryRepository.findAll();
        assertThat(questionCategoryList).hasSize(databaseSizeBeforeUpdate);
        QuestionCategory testQuestionCategory = questionCategoryList.get(questionCategoryList.size() - 1);
        assertThat(testQuestionCategory.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingQuestionCategory() throws Exception {
        int databaseSizeBeforeUpdate = questionCategoryRepository.findAll().size();
        questionCategory.setId(count.incrementAndGet());

        // Create the QuestionCategory
        QuestionCategoryDTO questionCategoryDTO = questionCategoryMapper.toDto(questionCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionCategory in the database
        List<QuestionCategory> questionCategoryList = questionCategoryRepository.findAll();
        assertThat(questionCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestionCategory() throws Exception {
        int databaseSizeBeforeUpdate = questionCategoryRepository.findAll().size();
        questionCategory.setId(count.incrementAndGet());

        // Create the QuestionCategory
        QuestionCategoryDTO questionCategoryDTO = questionCategoryMapper.toDto(questionCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionCategory in the database
        List<QuestionCategory> questionCategoryList = questionCategoryRepository.findAll();
        assertThat(questionCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestionCategory() throws Exception {
        int databaseSizeBeforeUpdate = questionCategoryRepository.findAll().size();
        questionCategory.setId(count.incrementAndGet());

        // Create the QuestionCategory
        QuestionCategoryDTO questionCategoryDTO = questionCategoryMapper.toDto(questionCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionCategoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionCategory in the database
        List<QuestionCategory> questionCategoryList = questionCategoryRepository.findAll();
        assertThat(questionCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionCategoryWithPatch() throws Exception {
        // Initialize the database
        questionCategoryRepository.saveAndFlush(questionCategory);

        int databaseSizeBeforeUpdate = questionCategoryRepository.findAll().size();

        // Update the questionCategory using partial update
        QuestionCategory partialUpdatedQuestionCategory = new QuestionCategory();
        partialUpdatedQuestionCategory.setId(questionCategory.getId());

        partialUpdatedQuestionCategory.name(UPDATED_NAME);

        restQuestionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionCategory))
            )
            .andExpect(status().isOk());

        // Validate the QuestionCategory in the database
        List<QuestionCategory> questionCategoryList = questionCategoryRepository.findAll();
        assertThat(questionCategoryList).hasSize(databaseSizeBeforeUpdate);
        QuestionCategory testQuestionCategory = questionCategoryList.get(questionCategoryList.size() - 1);
        assertThat(testQuestionCategory.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateQuestionCategoryWithPatch() throws Exception {
        // Initialize the database
        questionCategoryRepository.saveAndFlush(questionCategory);

        int databaseSizeBeforeUpdate = questionCategoryRepository.findAll().size();

        // Update the questionCategory using partial update
        QuestionCategory partialUpdatedQuestionCategory = new QuestionCategory();
        partialUpdatedQuestionCategory.setId(questionCategory.getId());

        partialUpdatedQuestionCategory.name(UPDATED_NAME);

        restQuestionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionCategory))
            )
            .andExpect(status().isOk());

        // Validate the QuestionCategory in the database
        List<QuestionCategory> questionCategoryList = questionCategoryRepository.findAll();
        assertThat(questionCategoryList).hasSize(databaseSizeBeforeUpdate);
        QuestionCategory testQuestionCategory = questionCategoryList.get(questionCategoryList.size() - 1);
        assertThat(testQuestionCategory.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingQuestionCategory() throws Exception {
        int databaseSizeBeforeUpdate = questionCategoryRepository.findAll().size();
        questionCategory.setId(count.incrementAndGet());

        // Create the QuestionCategory
        QuestionCategoryDTO questionCategoryDTO = questionCategoryMapper.toDto(questionCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionCategory in the database
        List<QuestionCategory> questionCategoryList = questionCategoryRepository.findAll();
        assertThat(questionCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestionCategory() throws Exception {
        int databaseSizeBeforeUpdate = questionCategoryRepository.findAll().size();
        questionCategory.setId(count.incrementAndGet());

        // Create the QuestionCategory
        QuestionCategoryDTO questionCategoryDTO = questionCategoryMapper.toDto(questionCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionCategory in the database
        List<QuestionCategory> questionCategoryList = questionCategoryRepository.findAll();
        assertThat(questionCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestionCategory() throws Exception {
        int databaseSizeBeforeUpdate = questionCategoryRepository.findAll().size();
        questionCategory.setId(count.incrementAndGet());

        // Create the QuestionCategory
        QuestionCategoryDTO questionCategoryDTO = questionCategoryMapper.toDto(questionCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionCategory in the database
        List<QuestionCategory> questionCategoryList = questionCategoryRepository.findAll();
        assertThat(questionCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestionCategory() throws Exception {
        // Initialize the database
        questionCategoryRepository.saveAndFlush(questionCategory);

        int databaseSizeBeforeDelete = questionCategoryRepository.findAll().size();

        // Delete the questionCategory
        restQuestionCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, questionCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuestionCategory> questionCategoryList = questionCategoryRepository.findAll();
        assertThat(questionCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
