package com.payu.myshop.inventarioms.domain.ports.services;

import com.payu.myshop.inventarioms.domain.models.dto.Inventario;
import com.payu.myshop.inventarioms.domain.models.endpoints.ResponseWsInventario;

import java.util.List;

public interface InventarioService {

    ResponseWsInventario createProduct(Inventario request);

    ResponseWsInventario getProductList();

    ResponseWsInventario getProductById(Long idInventario);

    ResponseWsInventario updateProduct(Inventario request);

    ResponseWsInventario deleteProduct(Long idInventario);

    ResponseWsInventario updateStock(List<Inventario> listaProductos, String accion, Long idVenta);
}
