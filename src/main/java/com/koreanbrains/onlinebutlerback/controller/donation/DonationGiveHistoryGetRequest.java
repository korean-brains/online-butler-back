package com.koreanbrains.onlinebutlerback.controller.donation;

import com.koreanbrains.onlinebutlerback.common.page.PageRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class DonationGiveHistoryGetRequest extends PageRequest {
    private LocalDateTime start;
    private LocalDateTime end;
}
