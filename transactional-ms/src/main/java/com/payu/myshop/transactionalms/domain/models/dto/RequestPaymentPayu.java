package com.payu.myshop.transactionalms.domain.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestPaymentPayu {

    String language;
    String command;
    MerchantReq merchant;
    TransactionReq transaction;
    Boolean test;

}
