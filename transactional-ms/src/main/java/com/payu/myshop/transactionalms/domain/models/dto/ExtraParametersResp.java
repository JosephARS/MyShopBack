package com.payu.myshop.transactionalms.domain.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtraParametersResp {

    String BANK_REFERENCED_CODE;
}
