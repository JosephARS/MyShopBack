package com.payu.myshop.transactionalms.domain.models.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseCreateTokenPayu {

    String code;
    String error;
    CreditCardToken creditCardToken;
}
