package com.quizzly.service;

import com.quizzly.service.dto.UserAccountDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.quizzly.domain.UserAccount}.
 */
public interface UserAccountService {
    /**
     * Save a userAccount.
     *
     * @param userAccountDTO the entity to save.
     * @return the persisted entity.
     */
    UserAccountDTO save(UserAccountDTO userAccountDTO);

    /**
     * Partially updates a userAccount.
     *
     * @param userAccountDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserAccountDTO> partialUpdate(UserAccountDTO userAccountDTO);

    /**
     * Get all the userAccounts.
     *
     * @return the list of entities.
     */
    List<UserAccountDTO> findAll();

    /**
     * Get the "id" userAccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserAccountDTO> findOne(Long id);

    /**
     * Delete the "id" userAccount.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
