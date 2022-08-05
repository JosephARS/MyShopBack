package com.payu.myshop.transactionalms.domain.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditCardToken {

    String payerId;
    String name;
    String identificationNumber;
    String paymentMethod;
    String number;
    String expirationDate;
    String creditCardTokenId;
    Date creationDate;
    String maskedNumber;
    String errorDescription;

}
