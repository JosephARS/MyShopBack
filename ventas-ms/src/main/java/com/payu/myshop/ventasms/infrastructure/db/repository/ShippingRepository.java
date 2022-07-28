package com.payu.myshop.ventasms.infrastructure.db.repository;

import com.payu.myshop.ventasms.infrastructure.db.entities.ClienteEntity;
import com.payu.myshop.ventasms.infrastructure.db.entities.ShippingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingRepository extends JpaRepository<ShippingEntity, Long> {

    List<ShippingEntity> findByCliente(ClienteEntity cliente);
}
