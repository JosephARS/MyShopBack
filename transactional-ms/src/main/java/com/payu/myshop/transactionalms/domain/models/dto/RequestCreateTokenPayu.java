package com.payu.myshop.transactionalms.domain.models.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestCreateTokenPayu {

    String language;
    String command;
    MerchantReq merchant;
    CreditCardToken creditCardToken;
}
