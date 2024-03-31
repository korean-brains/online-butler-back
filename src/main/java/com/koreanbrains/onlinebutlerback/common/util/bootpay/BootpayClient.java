package com.koreanbrains.onlinebutlerback.common.util.bootpay;


import com.koreanbrains.onlinebutlerback.common.exception.BootpayException;
import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import kr.co.bootpay.Bootpay;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class BootpayClient {

    private final String applicationId;
    private final String privateKey;

    public BootpayClient(@Value("${boot-pay.application-id}") String applicationId, @Value("${boot-pay.private-key}") String privateKey) {
        this.applicationId = applicationId;
        this.privateKey = privateKey;
    }

    public HashMap<String, Object> getReceipt(String receiptId) {
        try {
            Bootpay bootpay = new Bootpay(applicationId, privateKey);
            HashMap<String, Object> token = bootpay.getAccessToken();
            if(token.get("error_code") != null) { //failed
                throw new BootpayException(ErrorCode.BOOTPAY_ACCESSTOKEN);
            }

            return bootpay.getReceipt(receiptId);
        } catch (Exception e) {
            throw new BootpayException(e, ErrorCode.BOOTPAY_ERORR);
        }
    }

}
