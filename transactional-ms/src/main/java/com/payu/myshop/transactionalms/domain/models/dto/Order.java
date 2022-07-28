package com.payu.myshop.transactionalms.domain.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {

    String accountId;
    String referenceCode;
    String description;
    String language;
    String signature;
    String notifyUrl;
    AdditionalValues additionalValues;
    Buyer buyer;
    ShippingAddress shippingAddress;
}
