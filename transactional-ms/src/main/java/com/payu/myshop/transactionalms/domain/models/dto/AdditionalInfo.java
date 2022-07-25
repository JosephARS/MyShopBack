package com.payu.myshop.transactionalms.domain.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalInfo {

    String paymentNetwork;
    String rejectionType;
    String responseNetworkMessage;
    String travelAgencyAuthorizationCode;
    String cardType;
    String transactionType;
}
