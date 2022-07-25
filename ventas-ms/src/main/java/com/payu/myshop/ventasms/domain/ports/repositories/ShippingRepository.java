package com.payu.myshop.ventasms.domain.ports.repositories;

import com.payu.myshop.ventasms.infrastructure.db.entities.ClienteEntity;
import com.payu.myshop.ventasms.infrastructure.db.entities.ShippingEntity;
import com.payu.myshop.ventasms.infrastructure.db.entities.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShippingRepository extends JpaRepository<ShippingEntity, Long> {

    List<ShippingEntity> findByCliente(ClienteEntity cliente);
}
