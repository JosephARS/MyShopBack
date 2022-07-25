package com.payu.myshop.ventasms.domain.models.dto;

import com.payu.myshop.ventasms.domain.models.dto.Cliente;
import com.payu.myshop.ventasms.domain.models.dto.DetalleVenta;
import com.payu.myshop.ventasms.domain.models.dto.Shipping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    Cliente cliente;
    Shipping shipping;
    List<DetalleVenta> listaProductos;

}
