package com.payu.myshop.ventasms.domain.models.dto;

import com.payu.myshop.ventasms.infrastructure.db.entities.VentaEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetalleVenta {

    Long idDetalleVenta;

    Long cantidadCompra;

    Long precio;

    Long idInventario;

}
