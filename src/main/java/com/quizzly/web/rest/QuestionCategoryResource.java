package com.quizzly.web.rest;

import com.quizzly.repository.QuestionCategoryRepository;
import com.quizzly.service.QuestionCategoryService;
import com.quizzly.service.dto.QuestionCategoryDTO;
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
 * REST controller for managing {@link com.quizzly.domain.QuestionCategory}.
 */
@RestController
@RequestMapping("/api")
public class QuestionCategoryResource {

    private final Logger log = LoggerFactory.getLogger(QuestionCategoryResource.class);

    private static final String ENTITY_NAME = "questionCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionCategoryService questionCategoryService;

    private final QuestionCategoryRepository questionCategoryRepository;

    public QuestionCategoryResource(
        QuestionCategoryService questionCategoryService,
        QuestionCategoryRepository questionCategoryRepository
    ) {
        this.questionCategoryService = questionCategoryService;
        this.questionCategoryRepository = questionCategoryRepository;
    }

    /**
     * {@code POST  /question-categories} : Create a new questionCategory.
     *
     * @param questionCategoryDTO the questionCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionCategoryDTO, or with status {@code 400 (Bad Request)} if the questionCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/question-categories")
    public ResponseEntity<QuestionCategoryDTO> createQuestionCategory(@RequestBody QuestionCategoryDTO questionCategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save QuestionCategory : {}", questionCategoryDTO);
        if (questionCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new questionCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuestionCategoryDTO result = questionCategoryService.save(questionCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/question-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /question-categories/:id} : Updates an existing questionCategory.
     *
     * @param id the id of the questionCategoryDTO to save.
     * @param questionCategoryDTO the questionCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the questionCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/question-categories/{id}")
    public ResponseEntity<QuestionCategoryDTO> updateQuestionCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionCategoryDTO questionCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update QuestionCategory : {}, {}", id, questionCategoryDTO);
        if (questionCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QuestionCategoryDTO result = questionCategoryService.save(questionCategoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, questionCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /question-categories/:id} : Partial updates given fields of an existing questionCategory, field will ignore if it is null
     *
     * @param id the id of the questionCategoryDTO to save.
     * @param questionCategoryDTO the questionCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the questionCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the questionCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/question-categories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<QuestionCategoryDTO> partialUpdateQuestionCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionCategoryDTO questionCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update QuestionCategory partially : {}, {}", id, questionCategoryDTO);
        if (questionCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuestionCategoryDTO> result = questionCategoryService.partialUpdate(questionCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, questionCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /question-categories} : get all the questionCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questionCategories in body.
     */
    @GetMapping("/question-categories")
    public List<QuestionCategoryDTO> getAllQuestionCategories() {
        log.debug("REST request to get all QuestionCategories");
        return questionCategoryService.findAll();
    }

    /**
     * {@code GET  /question-categories/:id} : get the "id" questionCategory.
     *
     * @param id the id of the questionCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/question-categories/{id}")
    public ResponseEntity<QuestionCategoryDTO> getQuestionCategory(@PathVariable Long id) {
        log.debug("REST request to get QuestionCategory : {}", id);
        Optional<QuestionCategoryDTO> questionCategoryDTO = questionCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionCategoryDTO);
    }

    /**
     * {@code DELETE  /question-categories/:id} : delete the "id" questionCategory.
     *
     * @param id the id of the questionCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/question-categories/{id}")
    public ResponseEntity<Void> deleteQuestionCategory(@PathVariable Long id) {
        log.debug("REST request to delete QuestionCategory : {}", id);
        questionCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
