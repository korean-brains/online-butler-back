package com.koreanbrains.onlinebutlerback.controller.auth;

public record SignupRequest(String email, String password, String name) {
}
