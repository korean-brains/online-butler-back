package com.koreanbrains.onlinebutlerback.controller.donation;

import com.koreanbrains.onlinebutlerback.common.ControllerTest;
import com.koreanbrains.onlinebutlerback.common.context.WithRestMockUser;
import com.koreanbrains.onlinebutlerback.common.page.Page;
import com.koreanbrains.onlinebutlerback.repository.donation.DonationGiveHistoryDto;
import com.koreanbrains.onlinebutlerback.repository.donation.DonationQueryRepository;
import com.koreanbrains.onlinebutlerback.repository.donation.DonationReceiveHistoryDto;
import com.koreanbrains.onlinebutlerback.service.donation.DonationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DonationController.class)
class DonationControllerTest extends ControllerTest {

    @MockBean
    DonationService donationService;
    @MockBean
    DonationQueryRepository donationQueryRepository;

    @Test
    @DisplayName("클라이언트가 진행한 결제가 유효한지 검증한다.")
    @WithRestMockUser
    void verify() throws Exception {
        // given
        doNothing().when(donationService).save(anyString(), anyLong(), anyLong(), anyString());
        DonationRequest donationRequest = new DonationRequest("66094df500be0400302256f1", 1L, 2L, "message");

        // when
        ResultActions result = mockMvc.perform(post("/api/donation/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(donationRequest)));

        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("후원 한 내역을 페이지네이션으로 조회한다.")
    @WithRestMockUser
    void findGiveHistoryPagination() throws Exception {
        // given
        List<DonationGiveHistoryDto> content = List.of(
                new DonationGiveHistoryDto(1L, "kim", 1000, LocalDateTime.of(2024, 4, 1, 0, 0)),
                new DonationGiveHistoryDto(2L, "kim", 1000, LocalDateTime.of(2024, 4, 1, 0, 1)),
                new DonationGiveHistoryDto(3L, "kim", 1000, LocalDateTime.of(2024, 4, 1, 0, 2)),
                new DonationGiveHistoryDto(4L, "kim", 1000, LocalDateTime.of(2024, 4, 1, 0, 3)),
                new DonationGiveHistoryDto(5L, "kim", 1000, LocalDateTime.of(2024, 4, 1, 0, 4))
        );
        given(donationQueryRepository.findGiveHistory(anyLong(), anyInt(), anyInt(), any(), any()))
                .willReturn(new Page<>(content, 1, 5, 10));

        DonationGiveHistoryGetRequest request = DonationGiveHistoryGetRequest.builder()
                .size(5)
                .number(1)
                .start(LocalDateTime.of(2024, 4, 1, 0, 0))
                .end(LocalDateTime.of(2024, 4, 30, 0, 0))
                .build();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        // when
        ResultActions result = mockMvc.perform(get("/api/donation/give")
                .param("size", String.valueOf(request.getSize()))
                .param("number", String.valueOf(request.getNumber()))
                .param("start", request.getStart().format(dateTimeFormatter))
                .param("end", request.getEnd().format(dateTimeFormatter)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(5))
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.hasNext").value(true))
                .andExpect(jsonPath("$.first").value(true));
    }

    @Test
    @DisplayName("후원 받은 내역을 페이지네이션으로 조회한다.")
    @WithRestMockUser
    void findReceiveHistoryPagination() throws Exception {
        // given
        List<DonationReceiveHistoryDto> content = List.of(
                new DonationReceiveHistoryDto(1L, "kim", 1000, LocalDateTime.of(2024, 4, 1, 0, 0)),
                new DonationReceiveHistoryDto(2L, "kim", 1000, LocalDateTime.of(2024, 4, 1, 0, 1)),
                new DonationReceiveHistoryDto(3L, "kim", 1000, LocalDateTime.of(2024, 4, 1, 0, 2)),
                new DonationReceiveHistoryDto(4L, "kim", 1000, LocalDateTime.of(2024, 4, 1, 0, 3)),
                new DonationReceiveHistoryDto(5L, "kim", 1000, LocalDateTime.of(2024, 4, 1, 0, 4))
        );
        given(donationQueryRepository.findReceiveHistory(anyLong(), anyInt(), anyInt(), any(), any()))
                .willReturn(new Page<>(content, 1, 5, 10));

        DonationReceiveHistoryGetRequest request = DonationReceiveHistoryGetRequest.builder()
                .size(5)
                .number(1)
                .start(LocalDateTime.of(2024, 4, 1, 0, 0))
                .end(LocalDateTime.of(2024, 4, 30, 0, 0))
                .build();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        // when
        ResultActions result = mockMvc.perform(get("/api/donation/receive")
                .param("size", String.valueOf(request.getSize()))
                .param("number", String.valueOf(request.getNumber()))
                .param("start", request.getStart().format(dateTimeFormatter))
                .param("end", request.getEnd().format(dateTimeFormatter)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(5))
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.hasNext").value(true))
                .andExpect(jsonPath("$.first").value(true));
    }

}