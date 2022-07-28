package com.payu.myshop.ventasms.domain.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetalleVenta {

    Long idDetalleVenta;

    Long cantidadCompra;

    Long precio;

    Long idInventario;

}
