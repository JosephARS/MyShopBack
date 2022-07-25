package com.payu.myshop.transactionalms.infrastructure.db.entities;

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

}
