package com.koreanbrains.onlinebutlerback.controller.donation;

import com.koreanbrains.onlinebutlerback.common.page.Page;
import com.koreanbrains.onlinebutlerback.common.page.PageRequest;
import com.koreanbrains.onlinebutlerback.common.security.annotation.AuthUser;
import com.koreanbrains.onlinebutlerback.common.security.dto.AccountDto;
import com.koreanbrains.onlinebutlerback.repository.donation.DonationGiveHistoryDto;
import com.koreanbrains.onlinebutlerback.repository.donation.DonationQueryRepository;
import com.koreanbrains.onlinebutlerback.repository.donation.DonationReceiveHistoryDto;
import com.koreanbrains.onlinebutlerback.repository.donation.DonationReceiveRankingDto;
import com.koreanbrains.onlinebutlerback.service.donation.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/donation")
public class DonationController {

    private final DonationService donationService;
    private final DonationQueryRepository donationQueryRepository;

    @PostMapping("/verify")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void verify(@RequestBody DonationRequest request) {
        donationService.save(request.receiptId(), request.giverId(), request.receiverId(), request.message());
    }

    @GetMapping("/give")
    @PreAuthorize("isAuthenticated()")
    public Page<DonationGiveHistoryDto> getGiveHistory(@AuthUser AccountDto accountDto,
                                                       @ModelAttribute DonationGiveHistoryGetRequest request) {

        return donationQueryRepository.findGiveHistory(accountDto.getId(), request.getSize(), request.getNumber(), request.getStart(), request.getEnd());
    }

    @GetMapping("/receive")
    @PreAuthorize("isAuthenticated()")
    public Page<DonationReceiveHistoryDto> getReceiveHistory(@AuthUser AccountDto accountDto,
                                                             @ModelAttribute DonationReceiveHistoryGetRequest request) {

        return donationQueryRepository.findReceiveHistory(accountDto.getId(), request.getSize(), request.getNumber(), request.getStart(), request.getEnd());
    }

    @GetMapping("/ranking")
    @PreAuthorize("isAuthenticated()")
    public Page<DonationReceiveRankingDto> getDonationRanking(@AuthUser AccountDto accountDto,
                                                              @ModelAttribute PageRequest request) {

        return donationQueryRepository.findReceiveRanking(accountDto.getId(), request.getSize(), request.getNumber());
    }

}
