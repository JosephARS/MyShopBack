package com.payu.myshop.ventasms.domain.ports.services;

import com.payu.myshop.ventasms.domain.models.dto.Venta;
import com.payu.myshop.ventasms.domain.models.endpoints.ResponseWS;

public interface VentaService {

    ResponseWS confirmarVenta(Venta request, String idtoken, String maskedCardNumber, String franquicia);

    ResponseWS consultarVentas();

    ResponseWS anularVenta(Venta request);

    ResponseWS consultarUsuario(String email);


}
