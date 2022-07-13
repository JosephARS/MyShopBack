package com.payu.myshop.inventarioms.infrastructure.entities;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URL;

@Data
@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Inventario",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"nombre", "imgUrl"})
)
public class InventarioEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idInventario;

    @Column(name = "nombre",nullable = false, length = 50)
    String nombre;

    @Column(name = "imgUrl",nullable = false, unique = true)
    String imgUrl;

    @Column(name = "precio",nullable = false)
    Long precio;

    @Column(name = "descripcion", length = 50)
    String descripcion;

    @Column(name = "stock",nullable = false, length = 100)
    Long stock;

    @Column(name = "activo",nullable = false)
    Boolean activo;

}
