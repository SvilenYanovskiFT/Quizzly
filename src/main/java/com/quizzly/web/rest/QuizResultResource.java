package com.quizzly.web.rest;

import com.quizzly.repository.QuizResultRepository;
import com.quizzly.service.QuizResultService;
import com.quizzly.service.dto.QuizResultDTO;
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
 * REST controller for managing {@link com.quizzly.domain.QuizResult}.
 */
@RestController
@RequestMapping("/api")
public class QuizResultResource {

    private final Logger log = LoggerFactory.getLogger(QuizResultResource.class);

    private static final String ENTITY_NAME = "quizResult";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuizResultService quizResultService;

    private final QuizResultRepository quizResultRepository;

    public QuizResultResource(QuizResultService quizResultService, QuizResultRepository quizResultRepository) {
        this.quizResultService = quizResultService;
        this.quizResultRepository = quizResultRepository;
    }

    /**
     * {@code POST  /quiz-results} : Create a new quizResult.
     *
     * @param quizResultDTO the quizResultDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quizResultDTO, or with status {@code 400 (Bad Request)} if the quizResult has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/quiz-results")
    public ResponseEntity<QuizResultDTO> createQuizResult(@RequestBody QuizResultDTO quizResultDTO) throws URISyntaxException {
        log.debug("REST request to save QuizResult : {}", quizResultDTO);
        if (quizResultDTO.getId() != null) {
            throw new BadRequestAlertException("A new quizResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuizResultDTO result = quizResultService.save(quizResultDTO);
        return ResponseEntity
            .created(new URI("/api/quiz-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /quiz-results/:id} : Updates an existing quizResult.
     *
     * @param id the id of the quizResultDTO to save.
     * @param quizResultDTO the quizResultDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizResultDTO,
     * or with status {@code 400 (Bad Request)} if the quizResultDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quizResultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/quiz-results/{id}")
    public ResponseEntity<QuizResultDTO> updateQuizResult(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuizResultDTO quizResultDTO
    ) throws URISyntaxException {
        log.debug("REST request to update QuizResult : {}, {}", id, quizResultDTO);
        if (quizResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizResultDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QuizResultDTO result = quizResultService.save(quizResultDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quizResultDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /quiz-results/:id} : Partial updates given fields of an existing quizResult, field will ignore if it is null
     *
     * @param id the id of the quizResultDTO to save.
     * @param quizResultDTO the quizResultDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizResultDTO,
     * or with status {@code 400 (Bad Request)} if the quizResultDTO is not valid,
     * or with status {@code 404 (Not Found)} if the quizResultDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the quizResultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/quiz-results/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<QuizResultDTO> partialUpdateQuizResult(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuizResultDTO quizResultDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update QuizResult partially : {}, {}", id, quizResultDTO);
        if (quizResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizResultDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuizResultDTO> result = quizResultService.partialUpdate(quizResultDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quizResultDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /quiz-results} : get all the quizResults.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quizResults in body.
     */
    @GetMapping("/quiz-results")
    public List<QuizResultDTO> getAllQuizResults() {
        log.debug("REST request to get all QuizResults");
        return quizResultService.findAll();
    }

    /**
     * {@code GET  /quiz-results/:id} : get the "id" quizResult.
     *
     * @param id the id of the quizResultDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quizResultDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/quiz-results/{id}")
    public ResponseEntity<QuizResultDTO> getQuizResult(@PathVariable Long id) {
        log.debug("REST request to get QuizResult : {}", id);
        Optional<QuizResultDTO> quizResultDTO = quizResultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quizResultDTO);
    }

    /**
     * {@code DELETE  /quiz-results/:id} : delete the "id" quizResult.
     *
     * @param id the id of the quizResultDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/quiz-results/{id}")
    public ResponseEntity<Void> deleteQuizResult(@PathVariable Long id) {
        log.debug("REST request to delete QuizResult : {}", id);
        quizResultService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
