package com.payu.myshop.ventasms.domain.usecases;

import com.payu.myshop.ventasms.domain.models.dto.Cliente;
import com.payu.myshop.ventasms.domain.models.dto.DetalleVenta;
import com.payu.myshop.ventasms.domain.models.dto.Venta;
import com.payu.myshop.ventasms.infrastructure.db.entities.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SampleFactory {

    ClienteEntity clienteFactory(Cliente oClienteReq){
        return ClienteEntity.builder()
                    .nombre(oClienteReq.getNombre())
                    .apellido(oClienteReq.getApellido())
                    .dni(oClienteReq.getDni())
                    .email(oClienteReq.getEmail())
                    .telefono(oClienteReq.getTelefono())
                .build();
    }

    ShippingEntity shippingFactory(Venta request, ClienteEntity cliente){
        return ShippingEntity.builder()
                    .direccion(request.getShipping().getDireccion())
                    .ciudad(request.getShipping().getCiudad())
                    .departamento(request.getShipping().getDepartamento())
                    .pais(request.getShipping().getPais())
                    .postalCode(request.getShipping().getPostalCode())
                    .cliente(cliente)
                .build();
    }

    DetalleVentaEntity detalleVentaFactory(Venta venta, DetalleVenta producto){
        return DetalleVentaEntity.builder()
                    .venta(new VentaEntity(venta))
                    .cantidad(producto.getCantidadCompra())
                    .precio(producto.getPrecio())
                    .idInventario(producto.getIdInventario())
                .build();
    }

    CardEntity cardFactory(String idToken, String franquicia, String maskedCardNumber, ClienteEntity cliente){
        return CardEntity.builder()
                    .token(idToken)
                    .franquicia(franquicia)
                    .creditCard(maskedCardNumber)
                    .cliente(cliente)
                .build();

    }






}
