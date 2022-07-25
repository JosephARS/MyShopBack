package com.payu.myshop.ventasms.domain.ports.repositories;

import com.payu.myshop.ventasms.infrastructure.db.entities.ClienteEntity;
import com.payu.myshop.ventasms.infrastructure.db.entities.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

    Optional<ClienteEntity> findByEmail(String email);


}
