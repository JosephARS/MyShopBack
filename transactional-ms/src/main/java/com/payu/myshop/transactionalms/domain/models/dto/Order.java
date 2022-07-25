package com.payu.myshop.transactionalms.domain.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
