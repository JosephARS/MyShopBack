package com.payu.myshop.inventarioms.domain.models.endpoints;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    Long idInventario;

    @NotEmpty(message = "El campo 'nombre' no puede ser nulo.")
    String nombre;

    String imgUrl;
    @NotNull(message = "El campo 'precio' no puede ser nulo.")
    @PositiveOrZero
    Long precio;

    @NotNull(message = "El campo 'descripcion' no puede ser nulo.")
    String descripcion;

    @NotNull(message = "El campo 'stock' no puede ser nulo.")
    @PositiveOrZero
    Long stock;

}
