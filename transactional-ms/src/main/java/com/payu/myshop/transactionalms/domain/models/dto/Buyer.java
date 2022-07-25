package com.payu.myshop.transactionalms.domain.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Buyer {

    String merchantBuyerId;
    String fullName;
    String emailAddress;
    String contactPhone;
    String dniNumber;
    ShippingAddress shippingAddress;
}
