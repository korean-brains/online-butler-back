package com.koreanbrains.onlinebutlerback.controller.donation;

public record DonationRequest(String paymentId, int amount, long giverId, long receiverId, String message) {
}
