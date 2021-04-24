package com.quizzly.service.impl;

import com.quizzly.domain.Quiz;
import com.quizzly.repository.QuizRepository;
import com.quizzly.service.QuizService;
import com.quizzly.service.dto.QuizDTO;
import com.quizzly.service.mapper.QuizMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Quiz}.
 */
@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    private final Logger log = LoggerFactory.getLogger(QuizServiceImpl.class);

    private final QuizRepository quizRepository;

    private final QuizMapper quizMapper;

    public QuizServiceImpl(QuizRepository quizRepository, QuizMapper quizMapper) {
        this.quizRepository = quizRepository;
        this.quizMapper = quizMapper;
    }

    @Override
    public QuizDTO save(QuizDTO quizDTO) {
        log.debug("Request to save Quiz : {}", quizDTO);
        Quiz quiz = quizMapper.toEntity(quizDTO);
        quiz = quizRepository.save(quiz);
        return quizMapper.toDto(quiz);
    }

    @Override
    public Optional<QuizDTO> partialUpdate(QuizDTO quizDTO) {
        log.debug("Request to partially update Quiz : {}", quizDTO);

        return quizRepository
            .findById(quizDTO.getId())
            .map(
                existingQuiz -> {
                    quizMapper.partialUpdate(existingQuiz, quizDTO);
                    return existingQuiz;
                }
            )
            .map(quizRepository::save)
            .map(quizMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuizDTO> findAll() {
        log.debug("Request to get all Quizzes");
        return quizRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(quizMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<QuizDTO> findAllWithEagerRelationships(Pageable pageable) {
        return quizRepository.findAllWithEagerRelationships(pageable).map(quizMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuizDTO> findOne(Long id) {
        log.debug("Request to get Quiz : {}", id);
        return quizRepository.findOneWithEagerRelationships(id).map(quizMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Quiz : {}", id);
        quizRepository.deleteById(id);
    }
}
