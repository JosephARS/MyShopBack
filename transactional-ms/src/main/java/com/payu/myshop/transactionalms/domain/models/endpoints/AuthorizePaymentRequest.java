package com.payu.myshop.transactionalms.domain.models.endpoints;

import com.payu.myshop.transactionalms.domain.models.dto.Buyer;
import com.payu.myshop.transactionalms.domain.models.dto.CreditCard;
import com.payu.myshop.transactionalms.domain.models.dto.Payer;
import com.payu.myshop.transactionalms.domain.models.dto.ShippingAddress;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthorizePaymentRequest {

    @NotNull(message = "El campo 'totalPrice' no puede ser nulo.")
    Long totalPrice;
    Long totalTax;
    @NotEmpty(message = "El campo 'currency' no puede ser nulo.")
    String currency;
    Buyer buyer;
    Payer payer;
    String creditCardTokenId;
    @Valid @NotNull(message = "El campo 'creditCard' no puede ser nulo.")
    CreditCard creditCard;
    @NotEmpty(message = "El campo 'paymentMethod' no puede ser nulo.")
    String paymentMethod;
    @NotEmpty(message = "El campo 'paymentCountry' no puede ser nulo.")
    String paymentCountry;

}
