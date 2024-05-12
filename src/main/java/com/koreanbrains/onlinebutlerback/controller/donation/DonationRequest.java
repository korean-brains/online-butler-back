package com.koreanbrains.onlinebutlerback.controller.donation;

public record DonationRequest(String receiptId, long giverId, long receiverId, String message) {
}
