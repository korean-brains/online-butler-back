package com.koreanbrains.onlinebutlerback.common.fixtures;

import java.util.HashMap;

public class DonationFixture {

    public static HashMap<String, Object> receipt() {
        return new HashMap<>(){{
           put("error_code", null);
           put("price", 1000);
           put("purchased_at", "2024-04-01T12:00:00+09:00");
        }};
    }

    public static HashMap<String, Object> receiptWithErrorCode() {
        return new HashMap<>(){{
           put("error_code", "APP_KEY_NOT_FOUND");
        }};
    }
}
