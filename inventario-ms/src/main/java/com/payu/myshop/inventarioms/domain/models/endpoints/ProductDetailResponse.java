package com.payu.myshop.inventarioms.domain.models.endpoints;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.net.URL;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties
public class ProductDetailResponse {

    Long idInventario;

    String nombre;

    String imgUrl;

    Long precio;

    String descripcion;

    Long stock;

}
