package com.payu.myshop.ventasms.domain.ports.repositories;

import com.payu.myshop.ventasms.infrastructure.db.entities.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends JpaRepository<VentaEntity, Long> {
}
