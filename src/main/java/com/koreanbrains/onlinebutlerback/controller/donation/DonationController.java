package com.koreanbrains.onlinebutlerback.controller.donation;

import com.koreanbrains.onlinebutlerback.common.page.Page;
import com.koreanbrains.onlinebutlerback.repository.donation.DonationGiveHistoryDto;
import com.koreanbrains.onlinebutlerback.repository.donation.DonationQueryRepository;
import com.koreanbrains.onlinebutlerback.repository.donation.DonationReceiveHistoryDto;
import com.koreanbrains.onlinebutlerback.service.donation.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/donation")
public class DonationController {

    private final DonationService donationService;
    private final DonationQueryRepository donationQueryRepository;

    @PostMapping("/verify")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verify(@RequestBody DonationRequest request) {
        donationService.save(request.receiptId(), request.giverId(), request.receiverId());
    }

    // TODO : giverId security 적용
    @GetMapping("/give")
    public Page<DonationGiveHistoryDto> getGiveHistory(@ModelAttribute DonationGiveHistoryGetRequest request) {
        return donationQueryRepository.findGiveHistory(1L, request.getSize(), request.getNumber(), request.getStart(), request.getEnd());
    }

    // TODO : giverId security 적용
    @GetMapping("/receive")
    public Page<DonationReceiveHistoryDto> getReceiveHistory(@ModelAttribute DonationReceiveHistoryGetRequest request) {
        return donationQueryRepository.findReceiveHistory(1L, request.getSize(), request.getNumber(), request.getStart(), request.getEnd());
    }

}
