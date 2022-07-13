package com.payu.myshop.inventarioms.domain.ports.repositories;

import com.payu.myshop.inventarioms.infrastructure.entities.InventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventarioRepository extends JpaRepository<InventarioEntity, Long> {


}
