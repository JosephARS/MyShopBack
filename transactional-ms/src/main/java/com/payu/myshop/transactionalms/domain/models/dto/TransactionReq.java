package com.payu.myshop.transactionalms.domain.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionReq {

    Order order;
    Payer payer;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    CreditCard creditCard;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String creditCardTokenId;
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
