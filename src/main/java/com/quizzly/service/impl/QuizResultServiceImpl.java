package com.quizzly.service.impl;

import com.quizzly.domain.QuizResult;
import com.quizzly.repository.QuizResultRepository;
import com.quizzly.service.QuizResultService;
import com.quizzly.service.dto.QuizResultDTO;
import com.quizzly.service.mapper.QuizResultMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link QuizResult}.
 */
@Service
@Transactional
public class QuizResultServiceImpl implements QuizResultService {

    private final Logger log = LoggerFactory.getLogger(QuizResultServiceImpl.class);

    private final QuizResultRepository quizResultRepository;

    private final QuizResultMapper quizResultMapper;

    public QuizResultServiceImpl(QuizResultRepository quizResultRepository, QuizResultMapper quizResultMapper) {
        this.quizResultRepository = quizResultRepository;
        this.quizResultMapper = quizResultMapper;
    }

    @Override
    public QuizResultDTO save(QuizResultDTO quizResultDTO) {
        log.debug("Request to save QuizResult : {}", quizResultDTO);
        QuizResult quizResult = quizResultMapper.toEntity(quizResultDTO);
        quizResult = quizResultRepository.save(quizResult);
        return quizResultMapper.toDto(quizResult);
    }

    @Override
    public Optional<QuizResultDTO> partialUpdate(QuizResultDTO quizResultDTO) {
        log.debug("Request to partially update QuizResult : {}", quizResultDTO);

        return quizResultRepository
            .findById(quizResultDTO.getId())
            .map(
                existingQuizResult -> {
                    quizResultMapper.partialUpdate(existingQuizResult, quizResultDTO);
                    return existingQuizResult;
                }
            )
            .map(quizResultRepository::save)
            .map(quizResultMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuizResultDTO> findAll() {
        log.debug("Request to get all QuizResults");
        return quizResultRepository.findAll().stream().map(quizResultMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuizResultDTO> findOne(Long id) {
        log.debug("Request to get QuizResult : {}", id);
        return quizResultRepository.findById(id).map(quizResultMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete QuizResult : {}", id);
        quizResultRepository.deleteById(id);
    }
}
