package com.koreanbrains.onlinebutlerback.common.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;
    private boolean isActive;

    public void setPassword(String password) {
        this.password = password;
    }
}
