package com.koreanbrains.onlinebutlerback.controller.settlement;

import com.koreanbrains.onlinebutlerback.common.ControllerTest;
import com.koreanbrains.onlinebutlerback.common.context.WithRestMockUser;
import com.koreanbrains.onlinebutlerback.common.fixtures.SettlementFixture;
import com.koreanbrains.onlinebutlerback.repository.settlement.SettlementQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SettlementController.class)
class SettlementControllerTest extends ControllerTest {

    @MockBean
    SettlementQueryRepository settlementQueryRepository;


    @Test
    @DisplayName("특정 기간의 정산 지급금 내역을 조회 한다")
    @WithRestMockUser
    void getSettlementHistory() throws Exception {
        // given
        given(settlementQueryRepository.getSettlementHistoryPageByPeriod(anyLong(), any(), any(), anyInt(), anyInt()))
                .willReturn(SettlementFixture.settlementHistoryDtoPage(1, 5, 20));

        // when
        ResultActions result = mockMvc.perform(get("/api/settlement")
                .param("number", "1")
                .param("size", "5")
                .param("start", "2024-01-01T00:00:00")
                .param("end", "2024-06-01T00:00:00"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.hasNext").exists())
                .andExpect(jsonPath("$.first").exists());
    }

    @Test
    @DisplayName("이번 달 후원 금액을 조회 한다")
    @WithRestMockUser
    void getMonthlyDonationAmount() throws Exception {
        // given
        given(settlementQueryRepository.getDonationAmountByPeriod(anyLong(), any(), any()))
                .willReturn(100000);

        // when
        ResultActions result = mockMvc.perform(get("/api/settlement/monthly"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(100000));
    }
}