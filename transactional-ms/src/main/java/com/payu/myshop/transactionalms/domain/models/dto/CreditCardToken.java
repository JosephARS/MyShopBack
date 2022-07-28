package com.payu.myshop.transactionalms.domain.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
