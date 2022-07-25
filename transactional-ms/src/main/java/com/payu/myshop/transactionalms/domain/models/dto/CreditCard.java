package com.payu.myshop.transactionalms.domain.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreditCard {

    String number;
    String securityCode;
    String expirationDate;
    String name;
}
