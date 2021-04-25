package com.quizzly.service.impl;

import com.quizzly.domain.UserAccount;
import com.quizzly.repository.UserAccountRepository;
import com.quizzly.service.UserAccountService;
import com.quizzly.service.dto.UserAccountDTO;
import com.quizzly.service.mapper.UserAccountMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserAccount}.
 */
@Service
@Transactional
public class UserAccountServiceImpl implements UserAccountService {

    private final Logger log = LoggerFactory.getLogger(UserAccountServiceImpl.class);

    private final UserAccountRepository userAccountRepository;

    private final UserAccountMapper userAccountMapper;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository, UserAccountMapper userAccountMapper) {
        this.userAccountRepository = userAccountRepository;
        this.userAccountMapper = userAccountMapper;
    }

    @Override
    public UserAccountDTO save(UserAccountDTO userAccountDTO) {
        log.debug("Request to save UserAccount : {}", userAccountDTO);
        UserAccount userAccount = userAccountMapper.toEntity(userAccountDTO);
        userAccount = userAccountRepository.save(userAccount);
        return userAccountMapper.toDto(userAccount);
    }

    @Override
    public Optional<UserAccountDTO> partialUpdate(UserAccountDTO userAccountDTO) {
        log.debug("Request to partially update UserAccount : {}", userAccountDTO);

        return userAccountRepository
            .findById(userAccountDTO.getId())
            .map(
                existingUserAccount -> {
                    userAccountMapper.partialUpdate(existingUserAccount, userAccountDTO);
                    return existingUserAccount;
                }
            )
            .map(userAccountRepository::save)
            .map(userAccountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserAccountDTO> findAll() {
        log.debug("Request to get all UserAccounts");
        return userAccountRepository.findAll().stream().map(userAccountMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAccountDTO> findOne(Long id) {
        log.debug("Request to get UserAccount : {}", id);
        return userAccountRepository.findById(id).map(userAccountMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserAccount : {}", id);
        userAccountRepository.deleteById(id);
    }
}
