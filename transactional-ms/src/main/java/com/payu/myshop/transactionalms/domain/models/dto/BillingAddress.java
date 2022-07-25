package com.payu.myshop.transactionalms.domain.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BillingAddress {

    String street1;
    String street2;
    String city;
    String state;
    String country;
    String postalCode;
    String phone;
}
