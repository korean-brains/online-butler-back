package com.koreanbrains.onlinebutlerback.controller.donation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koreanbrains.onlinebutlerback.service.donation.DonationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DonationController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class DonationControllerTest {

    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    DonationService donationService;

    @Test
    @DisplayName("클라이언트가 진행한 결제가 유효한지 검증한다.")
    void verify() throws Exception {
        // given
        doNothing().when(donationService).save(anyString(), anyLong(), anyLong());
        DonationRequest donationRequest = new DonationRequest("66094df500be0400302256f1", 1L, 2L);

        // when
        ResultActions result = mockMvc.perform(post("/donation/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(donationRequest)));

        // then
        result.andExpect(status().isNoContent());
    }


}