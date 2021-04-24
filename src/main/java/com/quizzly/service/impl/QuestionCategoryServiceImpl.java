package com.quizzly.service.impl;

import com.quizzly.domain.QuestionCategory;
import com.quizzly.repository.QuestionCategoryRepository;
import com.quizzly.service.QuestionCategoryService;
import com.quizzly.service.dto.QuestionCategoryDTO;
import com.quizzly.service.mapper.QuestionCategoryMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link QuestionCategory}.
 */
@Service
@Transactional
public class QuestionCategoryServiceImpl implements QuestionCategoryService {

    private final Logger log = LoggerFactory.getLogger(QuestionCategoryServiceImpl.class);

    private final QuestionCategoryRepository questionCategoryRepository;

    private final QuestionCategoryMapper questionCategoryMapper;

    public QuestionCategoryServiceImpl(
        QuestionCategoryRepository questionCategoryRepository,
        QuestionCategoryMapper questionCategoryMapper
    ) {
        this.questionCategoryRepository = questionCategoryRepository;
        this.questionCategoryMapper = questionCategoryMapper;
    }

    @Override
    public QuestionCategoryDTO save(QuestionCategoryDTO questionCategoryDTO) {
        log.debug("Request to save QuestionCategory : {}", questionCategoryDTO);
        QuestionCategory questionCategory = questionCategoryMapper.toEntity(questionCategoryDTO);
        questionCategory = questionCategoryRepository.save(questionCategory);
        return questionCategoryMapper.toDto(questionCategory);
    }

    @Override
    public Optional<QuestionCategoryDTO> partialUpdate(QuestionCategoryDTO questionCategoryDTO) {
        log.debug("Request to partially update QuestionCategory : {}", questionCategoryDTO);

        return questionCategoryRepository
            .findById(questionCategoryDTO.getId())
            .map(
                existingQuestionCategory -> {
                    questionCategoryMapper.partialUpdate(existingQuestionCategory, questionCategoryDTO);
                    return existingQuestionCategory;
                }
            )
            .map(questionCategoryRepository::save)
            .map(questionCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionCategoryDTO> findAll() {
        log.debug("Request to get all QuestionCategories");
        return questionCategoryRepository
            .findAll()
            .stream()
            .map(questionCategoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuestionCategoryDTO> findOne(Long id) {
        log.debug("Request to get QuestionCategory : {}", id);
        return questionCategoryRepository.findById(id).map(questionCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete QuestionCategory : {}", id);
        questionCategoryRepository.deleteById(id);
    }
}
