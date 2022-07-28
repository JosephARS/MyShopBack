package com.payu.myshop.transactionalms.domain.models.dto;

import com.payu.myshop.transactionalms.infrastructure.db.entities.PagosEntity;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Transaccion {

    Long idTransaccion;

    String payuTransaccionId;

    Date fecha;

    String state;

    String codeResponde;

    String networkResponseCode;

    String authorizationCode;

    String responseMessage;

    String rejectionType;

    String transactionType;

    Pago pago;
}
