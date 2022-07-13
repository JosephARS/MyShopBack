package com.payu.myshop.inventarioms.domain.ports.services;

import com.payu.myshop.inventarioms.domain.models.endpoints.ProductRequest;
import com.payu.myshop.inventarioms.domain.models.endpoints.ResponseWS;

public interface InventarioService {

    ResponseWS createProduct(ProductRequest request);

    ResponseWS getProductList();

    ResponseWS getProductById(Long idInventario);

    ResponseWS updateProduct(ProductRequest request);

    ResponseWS deleteProduct(Long idInventario);


}
