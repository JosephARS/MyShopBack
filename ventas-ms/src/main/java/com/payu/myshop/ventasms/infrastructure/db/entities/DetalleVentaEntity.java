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
@Table(name="DetalleVenta")
public class DetalleVentaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idDetalleVenta;

    Long cantidad;

    Long precio;

    Long idInventario;

    @ManyToOne(optional = false) @JoinColumn(name = "idVenta")
    VentaEntity venta;


}
