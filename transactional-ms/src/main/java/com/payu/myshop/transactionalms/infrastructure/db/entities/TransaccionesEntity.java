package com.payu.myshop.transactionalms.infrastructure.db.entities;

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
@Table(name="Transacciones")
public class TransaccionesEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idTransaccion;

    @Column(name = "payuTransaccionId", unique = true)
    String payuTransaccionId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha", nullable = false)
    Date fecha;

    @Column(name = "state")
    String state;

    @Column(name = "codeResponde")
    String codeResponde;

    @Column(name = "networkResponseCode")
    String networkResponseCode;

    @Column(name = "authorizationCode")
    String authorizationCode;

    @Column(name = "responseMessage")
    String responseMessage;

    @Column(name = "rejectionType")
    String rejectionType;

    @Column(name = "transactionType")
    String transactionType;

    @ManyToOne(optional = false) @JoinColumn(name = "idPago")
    PagosEntity pago;
}
