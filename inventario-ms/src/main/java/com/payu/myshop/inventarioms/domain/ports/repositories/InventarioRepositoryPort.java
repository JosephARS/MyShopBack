package com.payu.myshop.inventarioms.domain.ports.repositories;

import com.payu.myshop.inventarioms.domain.models.dto.Inventario;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioRepositoryPort {

    Inventario save(Inventario producto);
    List<Inventario> findAll();
    Inventario findById(Long idInventario);

    Inventario updateProduct(Inventario producto);

    Inventario deleteProduct(Long idInventario);

    void updateStock(List<Inventario> listaProductos, String accion);
}
