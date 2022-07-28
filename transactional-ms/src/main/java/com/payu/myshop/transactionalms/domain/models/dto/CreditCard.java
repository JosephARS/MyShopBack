package com.payu.myshop.transactionalms.domain.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditCard {

    String number;
    String securityCode;
    String expirationDate;
    String name;
}
