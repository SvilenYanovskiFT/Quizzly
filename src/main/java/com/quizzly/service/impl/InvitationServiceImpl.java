package com.quizzly.service.impl;

import com.quizzly.domain.Invitation;
import com.quizzly.repository.InvitationRepository;
import com.quizzly.service.InvitationService;
import com.quizzly.service.dto.InvitationDTO;
import com.quizzly.service.mapper.InvitationMapper;
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
 * Service Implementation for managing {@link Invitation}.
 */
@Service
@Transactional
public class InvitationServiceImpl implements InvitationService {

    private final Logger log = LoggerFactory.getLogger(InvitationServiceImpl.class);

    private final InvitationRepository invitationRepository;

    private final InvitationMapper invitationMapper;

    public InvitationServiceImpl(InvitationRepository invitationRepository, InvitationMapper invitationMapper) {
        this.invitationRepository = invitationRepository;
        this.invitationMapper = invitationMapper;
    }

    @Override
    public InvitationDTO save(InvitationDTO invitationDTO) {
        log.debug("Request to save Invitation : {}", invitationDTO);
        Invitation invitation = invitationMapper.toEntity(invitationDTO);
        invitation = invitationRepository.save(invitation);
        return invitationMapper.toDto(invitation);
    }

    @Override
    public Optional<InvitationDTO> partialUpdate(InvitationDTO invitationDTO) {
        log.debug("Request to partially update Invitation : {}", invitationDTO);

        return invitationRepository
            .findById(invitationDTO.getId())
            .map(
                existingInvitation -> {
                    invitationMapper.partialUpdate(existingInvitation, invitationDTO);
                    return existingInvitation;
                }
            )
            .map(invitationRepository::save)
            .map(invitationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvitationDTO> findAll() {
        log.debug("Request to get all Invitations");
        return invitationRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(invitationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<InvitationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return invitationRepository.findAllWithEagerRelationships(pageable).map(invitationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InvitationDTO> findOne(Long id) {
        log.debug("Request to get Invitation : {}", id);
        return invitationRepository.findOneWithEagerRelationships(id).map(invitationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Invitation : {}", id);
        invitationRepository.deleteById(id);
    }
}
