package com.payu.myshop.inventarioms.infrastructure.db.repository;

import com.payu.myshop.inventarioms.infrastructure.db.entities.InventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventarioRepository extends JpaRepository<InventarioEntity, Long> {


}
