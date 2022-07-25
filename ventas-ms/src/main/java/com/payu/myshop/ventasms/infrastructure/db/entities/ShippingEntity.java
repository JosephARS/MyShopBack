package com.payu.myshop.ventasms.infrastructure.db.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Shipping")
public class ShippingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idShipping;

    String direccion;
    String ciudad;
    String departamento;
    String pais;
    String postalCode;

    @ManyToOne(optional = false) @JoinColumn(name = "idCliente")
    ClienteEntity cliente;
}
