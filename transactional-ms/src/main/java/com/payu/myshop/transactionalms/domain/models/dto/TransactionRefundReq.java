package com.payu.myshop.transactionalms.domain.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionRefundReq {

    OrderRefund order;
    String type;
    String reason;
    String parentTransactionId;
}
