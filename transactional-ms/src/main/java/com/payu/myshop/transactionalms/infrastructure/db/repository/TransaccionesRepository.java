package com.payu.myshop.transactionalms.infrastructure.db.repository;

import com.payu.myshop.transactionalms.infrastructure.db.entities.PagosEntity;
import com.payu.myshop.transactionalms.infrastructure.db.entities.TransaccionesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionesRepository extends JpaRepository<TransaccionesEntity, Long> {

    List<TransaccionesEntity> findByPago(PagosEntity pago);

}
