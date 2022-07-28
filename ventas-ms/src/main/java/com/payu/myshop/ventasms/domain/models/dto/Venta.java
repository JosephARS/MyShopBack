package com.payu.myshop.ventasms.domain.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Venta {

    Long idVenta;
    Long idPago;
    String estado;
    Long valor;
    Date fecha;
    Cliente cliente;
    Shipping shipping;
    List<DetalleVenta> listaProductos;

}
