package com.payu.myshop.transactionalms.domain.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionReq {

    Order order;
    Payer payer;
    CreditCard creditCard;
    ExtraParameters extraParameters;
    String type;
    String paymentMethod;
    String paymentCountry;
    String deviceSessionId;
    String ipAddress;
    String cookie;
    String userAgent;
    ThreeDomainSecure threeDomainSecure;
}
