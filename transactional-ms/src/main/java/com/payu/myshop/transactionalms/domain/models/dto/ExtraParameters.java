package com.payu.myshop.transactionalms.domain.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExtraParameters {

    String INSTALLMENTS_NUMBER;
}
