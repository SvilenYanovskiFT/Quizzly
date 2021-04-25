package com.quizzly.service.impl;

import com.quizzly.domain.QuestionAnswer;
import com.quizzly.repository.QuestionAnswerRepository;
import com.quizzly.service.QuestionAnswerService;
import com.quizzly.service.dto.QuestionAnswerDTO;
import com.quizzly.service.mapper.QuestionAnswerMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link QuestionAnswer}.
 */
@Service
@Transactional
public class QuestionAnswerServiceImpl implements QuestionAnswerService {

    private final Logger log = LoggerFactory.getLogger(QuestionAnswerServiceImpl.class);

    private final QuestionAnswerRepository questionAnswerRepository;

    private final QuestionAnswerMapper questionAnswerMapper;

    public QuestionAnswerServiceImpl(QuestionAnswerRepository questionAnswerRepository, QuestionAnswerMapper questionAnswerMapper) {
        this.questionAnswerRepository = questionAnswerRepository;
        this.questionAnswerMapper = questionAnswerMapper;
    }

    @Override
    public QuestionAnswerDTO save(QuestionAnswerDTO questionAnswerDTO) {
        log.debug("Request to save QuestionAnswer : {}", questionAnswerDTO);
        QuestionAnswer questionAnswer = questionAnswerMapper.toEntity(questionAnswerDTO);
        questionAnswer = questionAnswerRepository.save(questionAnswer);
        return questionAnswerMapper.toDto(questionAnswer);
    }

    @Override
    public Optional<QuestionAnswerDTO> partialUpdate(QuestionAnswerDTO questionAnswerDTO) {
        log.debug("Request to partially update QuestionAnswer : {}", questionAnswerDTO);

        return questionAnswerRepository
            .findById(questionAnswerDTO.getId())
            .map(
                existingQuestionAnswer -> {
                    questionAnswerMapper.partialUpdate(existingQuestionAnswer, questionAnswerDTO);
                    return existingQuestionAnswer;
                }
            )
            .map(questionAnswerRepository::save)
            .map(questionAnswerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionAnswerDTO> findAll() {
        log.debug("Request to get all QuestionAnswers");
        return questionAnswerRepository
            .findAll()
            .stream()
            .map(questionAnswerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuestionAnswerDTO> findOne(Long id) {
        log.debug("Request to get QuestionAnswer : {}", id);
        return questionAnswerRepository.findById(id).map(questionAnswerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete QuestionAnswer : {}", id);
        questionAnswerRepository.deleteById(id);
    }
}
