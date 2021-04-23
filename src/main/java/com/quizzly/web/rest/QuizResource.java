package com.quizzly.web.rest;

import com.quizzly.repository.QuizRepository;
import com.quizzly.service.QuizService;
import com.quizzly.service.dto.QuizDTO;
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
 * REST controller for managing {@link com.quizzly.domain.Quiz}.
 */
@RestController
@RequestMapping("/api")
public class QuizResource {

    private final Logger log = LoggerFactory.getLogger(QuizResource.class);

    private static final String ENTITY_NAME = "quiz";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuizService quizService;

    private final QuizRepository quizRepository;

    public QuizResource(QuizService quizService, QuizRepository quizRepository) {
        this.quizService = quizService;
        this.quizRepository = quizRepository;
    }

    /**
     * {@code POST  /quizzes} : Create a new quiz.
     *
     * @param quizDTO the quizDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quizDTO, or with status {@code 400 (Bad Request)} if the quiz has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/quizzes")
    public ResponseEntity<QuizDTO> createQuiz(@RequestBody QuizDTO quizDTO) throws URISyntaxException {
        log.debug("REST request to save Quiz : {}", quizDTO);
        if (quizDTO.getId() != null) {
            throw new BadRequestAlertException("A new quiz cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuizDTO result = quizService.save(quizDTO);
        return ResponseEntity
            .created(new URI("/api/quizzes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /quizzes/:id} : Updates an existing quiz.
     *
     * @param id the id of the quizDTO to save.
     * @param quizDTO the quizDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizDTO,
     * or with status {@code 400 (Bad Request)} if the quizDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quizDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/quizzes/{id}")
    public ResponseEntity<QuizDTO> updateQuiz(@PathVariable(value = "id", required = false) final Long id, @RequestBody QuizDTO quizDTO)
        throws URISyntaxException {
        log.debug("REST request to update Quiz : {}, {}", id, quizDTO);
        if (quizDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QuizDTO result = quizService.save(quizDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quizDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /quizzes/:id} : Partial updates given fields of an existing quiz, field will ignore if it is null
     *
     * @param id the id of the quizDTO to save.
     * @param quizDTO the quizDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizDTO,
     * or with status {@code 400 (Bad Request)} if the quizDTO is not valid,
     * or with status {@code 404 (Not Found)} if the quizDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the quizDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/quizzes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<QuizDTO> partialUpdateQuiz(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuizDTO quizDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Quiz partially : {}, {}", id, quizDTO);
        if (quizDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuizDTO> result = quizService.partialUpdate(quizDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quizDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /quizzes} : get all the quizzes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quizzes in body.
     */
    @GetMapping("/quizzes")
    public List<QuizDTO> getAllQuizzes() {
        log.debug("REST request to get all Quizzes");
        return quizService.findAll();
    }

    /**
     * {@code GET  /quizzes/:id} : get the "id" quiz.
     *
     * @param id the id of the quizDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quizDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/quizzes/{id}")
    public ResponseEntity<QuizDTO> getQuiz(@PathVariable Long id) {
        log.debug("REST request to get Quiz : {}", id);
        Optional<QuizDTO> quizDTO = quizService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quizDTO);
    }

    /**
     * {@code DELETE  /quizzes/:id} : delete the "id" quiz.
     *
     * @param id the id of the quizDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        log.debug("REST request to delete Quiz : {}", id);
        quizService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
