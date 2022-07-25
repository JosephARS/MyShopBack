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
@Table(name="Card")
public class CardEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idCard;

    String token;
    String creditCard;
    String franquicia;

    @ManyToOne(optional = false) @JoinColumn(name = "idCliente")
    ClienteEntity cliente;

}
