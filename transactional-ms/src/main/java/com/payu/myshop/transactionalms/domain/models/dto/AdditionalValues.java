package com.payu.myshop.transactionalms.domain.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdditionalValues {

    @JsonProperty("TX_VALUE")
    TX_dto TX_VALUE;

    @JsonProperty("TX_TAX")
    TX_dto TX_TAX;

    @JsonProperty("TX_TAX_RETURN_BASE")
    TX_dto TX_TAX_RETURN_BASE;

}
