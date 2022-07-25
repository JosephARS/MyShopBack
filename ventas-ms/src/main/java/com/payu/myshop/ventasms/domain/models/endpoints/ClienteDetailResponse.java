package com.payu.myshop.ventasms.domain.models.endpoints;

import com.payu.myshop.ventasms.domain.models.dto.Card;
import com.payu.myshop.ventasms.domain.models.dto.Cliente;
import com.payu.myshop.ventasms.domain.models.dto.Shipping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDetailResponse {

    Cliente cliente;
    Shipping shipping;
    List<Card> cardList;

}
