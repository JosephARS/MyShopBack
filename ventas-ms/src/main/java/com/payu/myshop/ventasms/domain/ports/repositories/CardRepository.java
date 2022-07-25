package com.payu.myshop.ventasms.domain.ports.repositories;

import com.payu.myshop.ventasms.infrastructure.db.entities.CardEntity;
import com.payu.myshop.ventasms.infrastructure.db.entities.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {

    List<CardEntity> findByCliente(ClienteEntity cliente);
}
