package com.payu.myshop.ventasms.infrastructure.db.entities;

import com.payu.myshop.ventasms.domain.models.dto.Card;
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

    public CardEntity(Card card) {
        idCard = card.getIdCard();
        token = card.getToken();
        creditCard = card.getCreditCard();
        franquicia = card.getFranquicia();
        cliente = new ClienteEntity(card.getCliente());
    }

    public Card toDto(){
        return Card.builder()
                    .idCard(idCard)
                    .token(token)
                    .franquicia(franquicia)
                    .creditCard(creditCard)
                    .cliente(cliente.toDto())
                .build();
    }
}
