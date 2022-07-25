package com.payu.myshop.inventarioms.domain.ports.services;

import com.payu.myshop.inventarioms.domain.models.endpoints.Inventario;
import com.payu.myshop.inventarioms.domain.models.endpoints.ResponseWS;

import java.util.List;

public interface InventarioService {

    ResponseWS createProduct(Inventario request);

    ResponseWS getProductList();

    ResponseWS getProductById(Long idInventario);

    ResponseWS updateProduct(Inventario request);

    ResponseWS deleteProduct(Long idInventario);

    ResponseWS updateStock(List<Inventario> listaProductos, String accion);
}
