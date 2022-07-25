package com.payu.myshop.transactionalms.domain.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestRefundPayu {

    String language;
    String command;
    MerchantReq merchant;
    TransactionRefundReq transaction;
    Boolean test;
}
