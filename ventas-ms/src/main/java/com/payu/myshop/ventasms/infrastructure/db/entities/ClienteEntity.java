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
@Table(name="Cliente")
public class ClienteEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idCliente;

    String nombre;
    String apellido;
    String dni;
    String telefono;
    @Column(name = "email", unique = true)
    String email;
}
