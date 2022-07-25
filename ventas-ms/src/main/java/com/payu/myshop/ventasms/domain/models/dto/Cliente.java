package com.payu.myshop.ventasms.domain.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

    Long idCliente;
    String nombre;
    String apellido;
    String dni;
    String telefono;
    String email;
}
