package com.payu.myshop.transactionalms.infrastructure.db.entities;

import com.payu.myshop.transactionalms.domain.models.dto.Pago;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Pagos")
public class PagosEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idPago;

    @Column(name = "payuOrdenId", unique = true)
    Long payuOrdenId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha",nullable = false)
    Date fecha;

    @Column(name = "dniPayer")
    String dniPayer;

    @Column(name = "dniBuyer")
    String dniBuyer;

    @Column(name = "valor",nullable = false)
    Long valor;

    @Column(name = "direccion",nullable = false)
    String direccion;

    @Column(name = "ciudad",nullable = false)
    String ciudad;

    @Column(name = "pais",nullable = false)
    String pais;

    @Column(name = "metodoPago",nullable = false)
    String metodoPago;

    @Column(name = "franquicia",nullable = false)
    String franquicia;

    @Column(name = "numTarjeta")
    String numTarjeta;

    @Column(name = "tokenTarjeta")
    String tokenTarjeta;

    @Column(name = "estadoPago")
    String estadoPago;


    public PagosEntity(Pago pago) {
        idPago = pago.getIdPago();
        payuOrdenId = pago.getPayuOrdenId();
        fecha = pago.getFecha();
        dniPayer = pago.getDniPayer();
        dniBuyer = pago.getDniBuyer();
        valor = pago.getValor();
        direccion = pago.getDireccion();
        ciudad = pago.getCiudad();
        pais = pago.getPais();
        metodoPago = pago.getMetodoPago();
        franquicia =pago.getFranquicia();
        estadoPago = pago.getEstadoPago();

    }

    public Pago toDto(){
        return Pago.builder()
                .idPago(idPago)
                .payuOrdenId(payuOrdenId)
                .fecha(fecha)
                .dniPayer(dniPayer)
                .dniBuyer(dniBuyer)
                .valor(valor)
                .direccion(direccion)
                .ciudad(ciudad)
                .pais(pais)
                .metodoPago(metodoPago)
                .franquicia(franquicia)
                .estadoPago(estadoPago)
                .build();
    }
}
