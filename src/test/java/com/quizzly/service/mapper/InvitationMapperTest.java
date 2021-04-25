package com.quizzly.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvitationMapperTest {

    private InvitationMapper invitationMapper;

    @BeforeEach
    public void setUp() {
        invitationMapper = new InvitationMapperImpl();
    }
}
