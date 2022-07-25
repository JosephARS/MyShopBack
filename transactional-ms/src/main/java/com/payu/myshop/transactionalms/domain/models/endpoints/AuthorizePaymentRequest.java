package com.payu.myshop.transactionalms.domain.models.endpoints;

import com.payu.myshop.transactionalms.domain.models.dto.Buyer;
import com.payu.myshop.transactionalms.domain.models.dto.CreditCard;
import com.payu.myshop.transactionalms.domain.models.dto.Payer;
import com.payu.myshop.transactionalms.domain.models.dto.ShippingAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizePaymentRequest {

    Long totalPrice;
    Long totalTax;
    String currency;
    Buyer buyer;
    Payer payer;
    CreditCard creditCard;
    String paymentMethod;
    String paymentCountry;

}
