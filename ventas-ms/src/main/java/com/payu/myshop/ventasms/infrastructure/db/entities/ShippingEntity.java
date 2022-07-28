package com.payu.myshop.ventasms.infrastructure.db.entities;

import com.payu.myshop.ventasms.domain.models.dto.Shipping;
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

    public ShippingEntity(Shipping shipping) {
        idShipping = shipping.getIdShipping();
        direccion = shipping.getDireccion();
        ciudad = shipping.getCiudad();
        departamento = shipping.getDepartamento();
        pais = shipping.getPais();
        postalCode = shipping.getPostalCode();


    }

    public Shipping toDto(){
        return Shipping.builder()
                .idShipping(idShipping)
                .pais(pais)
                .direccion(direccion)
                .postalCode(postalCode)
                .departamento(departamento)
                .ciudad(ciudad)
                .build();
    }

}
