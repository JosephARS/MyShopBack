package com.payu.myshop.transactionalms.domain.models.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class Pago {

    Long idPago;

    Long payuOrdenId;

    Date fecha;

    String dniPayer;

    String dniBuyer;

    Long valor;

    String direccion;

    String ciudad;

    String pais;

    String metodoPago;

    String franquicia;

    String numTarjeta;

    String tokenTarjeta;

    String estadoPago;
}
