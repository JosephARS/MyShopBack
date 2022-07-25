package com.payu.myshop.transactionalms.domain.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ThreeDomainSecure {

    Boolean embedded;
    String eci;
    String cavv;
    String xid;
    String directoryServerTransactionId;

}
