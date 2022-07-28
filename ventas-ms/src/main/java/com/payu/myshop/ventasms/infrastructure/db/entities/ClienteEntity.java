package com.payu.myshop.ventasms.infrastructure.db.entities;

import com.payu.myshop.ventasms.domain.models.dto.Cliente;
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

    public ClienteEntity(Cliente client) {
        idCliente = client.getIdCliente();
        nombre = client.getNombre();
        apellido = client.getApellido();
        dni = client.getDni();;
        telefono = client.getTelefono();
        email = client.getEmail();

    }

    public Cliente toDto(){
        return Cliente.builder()
                .idCliente(idCliente)
                .telefono(telefono)
                .email(email)
                .dni(dni)
                .nombre(nombre)
                .apellido(apellido)
                .build();
    }
}
