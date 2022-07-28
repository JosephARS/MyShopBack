package com.payu.myshop.ventasms.infrastructure.db.entities;

import com.payu.myshop.ventasms.domain.models.dto.Venta;
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

    Long valor;

    @Temporal(TemporalType.TIMESTAMP)
    Date fecha;

    @ManyToOne(optional = false) @JoinColumn(name = "idCliente")
    ClienteEntity cliente;

    @OneToOne(optional = false) @JoinColumn(name = "idShipping")
    ShippingEntity shipping;

    Long idPago;

    String estado;

    public VentaEntity(Venta venta) {
        idVenta = venta.getIdVenta();
        valor = venta.getValor();
        fecha = venta.getFecha();
        cliente = new ClienteEntity(venta.getCliente());
        shipping = new ShippingEntity(venta.getShipping());
        idPago = venta.getIdPago();
        estado = venta.getEstado();
    }

    public Venta toDto(){
        return Venta.builder()
                .idVenta(idVenta)
                .cliente(cliente.toDto())
                .fecha(fecha)
                .idPago(idPago)
                .valor(valor)
                .estado(estado)
                .shipping(shipping.toDto())
                .build();
    }


}
