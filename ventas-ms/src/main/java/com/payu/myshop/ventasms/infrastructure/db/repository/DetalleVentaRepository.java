package com.payu.myshop.ventasms.infrastructure.db.repository;

import com.payu.myshop.ventasms.infrastructure.db.entities.DetalleVentaEntity;
import com.payu.myshop.ventasms.infrastructure.db.entities.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVentaEntity, Long> {

    List<DetalleVentaEntity> findByVenta(VentaEntity venta);
}
