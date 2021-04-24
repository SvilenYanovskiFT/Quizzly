package com.quizzly.web.rest;

import com.quizzly.repository.QuizRezultRepository;
import com.quizzly.service.QuizRezultService;
import com.quizzly.service.dto.QuizRezultDTO;
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
 * REST controller for managing {@link com.quizzly.domain.QuizRezult}.
 */
@RestController
@RequestMapping("/api")
public class QuizRezultResource {

    private final Logger log = LoggerFactory.getLogger(QuizRezultResource.class);

    private static final String ENTITY_NAME = "quizRezult";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuizRezultService quizRezultService;

    private final QuizRezultRepository quizRezultRepository;

    public QuizRezultResource(QuizRezultService quizRezultService, QuizRezultRepository quizRezultRepository) {
        this.quizRezultService = quizRezultService;
        this.quizRezultRepository = quizRezultRepository;
    }

    /**
     * {@code POST  /quiz-rezults} : Create a new quizRezult.
     *
     * @param quizRezultDTO the quizRezultDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quizRezultDTO, or with status {@code 400 (Bad Request)} if the quizRezult has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/quiz-rezults")
    public ResponseEntity<QuizRezultDTO> createQuizRezult(@RequestBody QuizRezultDTO quizRezultDTO) throws URISyntaxException {
        log.debug("REST request to save QuizRezult : {}", quizRezultDTO);
        if (quizRezultDTO.getId() != null) {
            throw new BadRequestAlertException("A new quizRezult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuizRezultDTO result = quizRezultService.save(quizRezultDTO);
        return ResponseEntity
            .created(new URI("/api/quiz-rezults/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /quiz-rezults/:id} : Updates an existing quizRezult.
     *
     * @param id the id of the quizRezultDTO to save.
     * @param quizRezultDTO the quizRezultDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizRezultDTO,
     * or with status {@code 400 (Bad Request)} if the quizRezultDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quizRezultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/quiz-rezults/{id}")
    public ResponseEntity<QuizRezultDTO> updateQuizRezult(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuizRezultDTO quizRezultDTO
    ) throws URISyntaxException {
        log.debug("REST request to update QuizRezult : {}, {}", id, quizRezultDTO);
        if (quizRezultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizRezultDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizRezultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QuizRezultDTO result = quizRezultService.save(quizRezultDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quizRezultDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /quiz-rezults/:id} : Partial updates given fields of an existing quizRezult, field will ignore if it is null
     *
     * @param id the id of the quizRezultDTO to save.
     * @param quizRezultDTO the quizRezultDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizRezultDTO,
     * or with status {@code 400 (Bad Request)} if the quizRezultDTO is not valid,
     * or with status {@code 404 (Not Found)} if the quizRezultDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the quizRezultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/quiz-rezults/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<QuizRezultDTO> partialUpdateQuizRezult(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuizRezultDTO quizRezultDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update QuizRezult partially : {}, {}", id, quizRezultDTO);
        if (quizRezultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizRezultDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizRezultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuizRezultDTO> result = quizRezultService.partialUpdate(quizRezultDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quizRezultDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /quiz-rezults} : get all the quizRezults.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quizRezults in body.
     */
    @GetMapping("/quiz-rezults")
    public List<QuizRezultDTO> getAllQuizRezults() {
        log.debug("REST request to get all QuizRezults");
        return quizRezultService.findAll();
    }

    /**
     * {@code GET  /quiz-rezults/:id} : get the "id" quizRezult.
     *
     * @param id the id of the quizRezultDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quizRezultDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/quiz-rezults/{id}")
    public ResponseEntity<QuizRezultDTO> getQuizRezult(@PathVariable Long id) {
        log.debug("REST request to get QuizRezult : {}", id);
        Optional<QuizRezultDTO> quizRezultDTO = quizRezultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quizRezultDTO);
    }

    /**
     * {@code DELETE  /quiz-rezults/:id} : delete the "id" quizRezult.
     *
     * @param id the id of the quizRezultDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/quiz-rezults/{id}")
    public ResponseEntity<Void> deleteQuizRezult(@PathVariable Long id) {
        log.debug("REST request to delete QuizRezult : {}", id);
        quizRezultService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
