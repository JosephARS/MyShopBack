package com.payu.myshop.transactionalms.domain.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class TransactionResponse {

    Long orderId;
    String transactionId;
    String state;
    String paymentNetworkResponseCode;
    String paymentNetworkResponseErrorMessage;
    String trazabilityCode;
    String authorizationCode;
    String pendingReason;
    String  responseCode;
    String errorCode;
    String responseMessage;
    String suggestedRetryPolicy;
    String transactionDate;
    String transactionTime;
    Date operationDate;
    String referenceQuestionnaire;
    ExtraParametersResp extraParameters;
    AdditionalInfo additionalInfo;


}
