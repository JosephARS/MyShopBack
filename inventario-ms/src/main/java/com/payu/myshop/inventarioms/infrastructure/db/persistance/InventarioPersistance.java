package com.payu.myshop.inventarioms.infrastructure.db.persistance;

import com.payu.myshop.inventarioms.domain.exceptions.NotFoundException;
import com.payu.myshop.inventarioms.domain.models.endpoints.Inventario;
import com.payu.myshop.inventarioms.domain.ports.repositories.InventarioRepositoryPort;
import com.payu.myshop.inventarioms.infrastructure.db.entities.InventarioEntity;
import com.payu.myshop.inventarioms.infrastructure.db.repository.InventarioRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository("inventarioRepositoryPort")
public class InventarioPersistance implements InventarioRepositoryPort {

    InventarioRepository inventarioRepository;

    public InventarioPersistance(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }
    @Override
    @Transactional
    public Inventario save(Inventario producto) {
        InventarioEntity inventario = inventarioRepository.save(InventarioEntity.builder()
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .imgUrl(producto.getImgUrl())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .activo(true)
                .build());

        return Inventario.builder()
                .nombre(inventario.getNombre())
                .descripcion(inventario.getDescripcion())
                .imgUrl(inventario.getImgUrl())
                .precio(inventario.getPrecio())
                .stock(inventario.getStock())
                .activo(inventario.getActivo())
                .build();
    }
    @Override
    public List<Inventario> findAll() {
        return inventarioRepository.findAll().stream().map(product -> Inventario.builder()
                .idInventario(product.getIdInventario())
                .nombre(product.getNombre())
                .descripcion(product.getDescripcion())
                .imgUrl(product.getImgUrl())
                .precio(product.getPrecio())
                .stock(product.getStock())
                .activo(product.getActivo())
                .build()).collect(Collectors.toList());
    }


    @Override
    public Inventario findById(Long idInventario) {
        return inventarioRepository.findById(idInventario).map(product -> Inventario.builder()
                .idInventario(product.getIdInventario())
                .nombre(product.getNombre())
                .descripcion(product.getDescripcion())
                .imgUrl(product.getImgUrl())
                .precio(product.getPrecio())
                .stock(product.getStock())
                .activo(product.getActivo())
                .build()).orElseThrow(() -> new NotFoundException("Producto no encontrado: " + idInventario));
    }

    @Override
    @Transactional
    public Inventario updateProduct(Inventario producto) {

        Optional<InventarioEntity> productToUpdate = inventarioRepository.findById(producto.getIdInventario());
        productToUpdate.get().setIdInventario(producto.getIdInventario());
        productToUpdate.get().setNombre(producto.getNombre());
        productToUpdate.get().setDescripcion(producto.getDescripcion());
        productToUpdate.get().setPrecio(producto.getPrecio());
        productToUpdate.get().setStock(producto.getStock());
        productToUpdate.get().setImgUrl(producto.getImgUrl());

        InventarioEntity inventario = inventarioRepository.save(productToUpdate.get());

        return Inventario.builder()
                .nombre(inventario.getNombre())
                .descripcion(inventario.getDescripcion())
                .imgUrl(inventario.getImgUrl())
                .precio(inventario.getPrecio())
                .stock(inventario.getStock())
                .activo(inventario.getActivo())
                .build();
    }

    @Override
    public Inventario deleteProduct(Long idInventario) {

        Optional<InventarioEntity> productToDelete = inventarioRepository.findById(idInventario);
        productToDelete.get().setActivo(false);

        InventarioEntity inventario = inventarioRepository.save(productToDelete.get());
        return Inventario.builder()
                .nombre(inventario.getNombre())
                .descripcion(inventario.getDescripcion())
                .imgUrl(inventario.getImgUrl())
                .precio(inventario.getPrecio())
                .stock(inventario.getStock())
                .activo(inventario.getActivo())
                .build();
    }

    @Override
    @Transactional
    public void updateStock(List<Inventario> listaProductos, String accion) {

        listaProductos.forEach(producto ->{
            long stock;

            Optional<InventarioEntity> productToUpdate = inventarioRepository.findById(producto.getIdInventario());

            if (accion.equalsIgnoreCase("venta")) {
                stock = productToUpdate.get().getStock() - producto.getCantidadCompra();
            }else {
                stock = productToUpdate.get().getStock() + producto.getCantidadCompra();
            }
            productToUpdate.get().setStock(stock);
            inventarioRepository.save(productToUpdate.get());
        });

    }

}
