package com.payu.myshop.ventasms.domain.ports.services;

import com.payu.myshop.ventasms.domain.models.dto.Venta;
import com.payu.myshop.ventasms.domain.models.endpoints.ResponseWsVentas;

public interface VentaService {

    ResponseWsVentas confirmSale(Venta request, String idtoken, String maskedCardNumber, String franquicia);

    ResponseWsVentas getSales();

    ResponseWsVentas refundSale(Venta request);

    ResponseWsVentas getUser(String email);

    ResponseWsVentas rollbackSale(Venta venta);


}
