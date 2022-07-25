package com.payu.myshop.transactionalms.domain.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payer {

    String merchantPayerId;
    String fullName;
    String emailAddress;
    String contactPhone;
    String dniNumber;
    BillingAddress billingAddress;
}
