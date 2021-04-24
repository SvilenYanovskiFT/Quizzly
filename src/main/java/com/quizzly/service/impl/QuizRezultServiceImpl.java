package com.quizzly.service.impl;

import com.quizzly.domain.QuizRezult;
import com.quizzly.repository.QuizRezultRepository;
import com.quizzly.service.QuizRezultService;
import com.quizzly.service.dto.QuizRezultDTO;
import com.quizzly.service.mapper.QuizRezultMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link QuizRezult}.
 */
@Service
@Transactional
public class QuizRezultServiceImpl implements QuizRezultService {

    private final Logger log = LoggerFactory.getLogger(QuizRezultServiceImpl.class);

    private final QuizRezultRepository quizRezultRepository;

    private final QuizRezultMapper quizRezultMapper;

    public QuizRezultServiceImpl(QuizRezultRepository quizRezultRepository, QuizRezultMapper quizRezultMapper) {
        this.quizRezultRepository = quizRezultRepository;
        this.quizRezultMapper = quizRezultMapper;
    }

    @Override
    public QuizRezultDTO save(QuizRezultDTO quizRezultDTO) {
        log.debug("Request to save QuizRezult : {}", quizRezultDTO);
        QuizRezult quizRezult = quizRezultMapper.toEntity(quizRezultDTO);
        quizRezult = quizRezultRepository.save(quizRezult);
        return quizRezultMapper.toDto(quizRezult);
    }

    @Override
    public Optional<QuizRezultDTO> partialUpdate(QuizRezultDTO quizRezultDTO) {
        log.debug("Request to partially update QuizRezult : {}", quizRezultDTO);

        return quizRezultRepository
            .findById(quizRezultDTO.getId())
            .map(
                existingQuizRezult -> {
                    quizRezultMapper.partialUpdate(existingQuizRezult, quizRezultDTO);
                    return existingQuizRezult;
                }
            )
            .map(quizRezultRepository::save)
            .map(quizRezultMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuizRezultDTO> findAll() {
        log.debug("Request to get all QuizRezults");
        return quizRezultRepository.findAll().stream().map(quizRezultMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuizRezultDTO> findOne(Long id) {
        log.debug("Request to get QuizRezult : {}", id);
        return quizRezultRepository.findById(id).map(quizRezultMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete QuizRezult : {}", id);
        quizRezultRepository.deleteById(id);
    }
}
