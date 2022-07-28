package com.payu.myshop.transactionalms.domain.models.endpoints;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizePaymentResponse {

    Long pagoId;
    String state;
    Long orderId;
    String creditCardTokenId;
    String maskedCardNumber;
    String transactionId;
    String authorizationCode;
    String networkResponseCode;
    String responseCode;
    String errorCode;
    String responseMessage;
    Date operationDate;
}
