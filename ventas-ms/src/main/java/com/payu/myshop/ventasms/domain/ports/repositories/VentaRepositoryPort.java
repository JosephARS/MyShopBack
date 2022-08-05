package com.payu.myshop.ventasms.domain.ports.repositories;

import com.payu.myshop.ventasms.domain.models.dto.Venta;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepositoryPort {

    Venta saveVenta(Venta venta);
    List<Object> findAllVentas();
    Venta updateRefund(Long idVenta);

    Venta rollabackVenta(Long idVenta);

}
