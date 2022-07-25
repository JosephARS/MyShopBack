package com.payu.myshop.ventasms.infrastructure.db.entities;

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
@Table(name="Venta")
public class VentaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idVenta;

    Long precio;

    @Temporal(TemporalType.TIMESTAMP)
    Date fecha;

    @ManyToOne(optional = false) @JoinColumn(name = "idCliente")
    ClienteEntity cliente;

    @OneToOne(optional = false) @JoinColumn(name = "idShipping")
    ShippingEntity shipping;

    Long idPago;

    String estado;
}
