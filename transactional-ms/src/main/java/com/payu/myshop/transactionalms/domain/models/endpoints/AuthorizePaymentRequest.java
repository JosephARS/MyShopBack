package com.payu.myshop.transactionalms.domain.models.endpoints;

import com.payu.myshop.transactionalms.domain.models.dto.Buyer;
import com.payu.myshop.transactionalms.domain.models.dto.CreditCard;
import com.payu.myshop.transactionalms.domain.models.dto.Payer;
import com.payu.myshop.transactionalms.domain.models.dto.ShippingAddress;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthorizePaymentRequest {

    Long totalPrice;
    Long totalTax;
    String currency;
    Buyer buyer;
    Payer payer;
    String creditCardTokenId;
    CreditCard creditCard;
    String paymentMethod;
    String paymentCountry;

}
