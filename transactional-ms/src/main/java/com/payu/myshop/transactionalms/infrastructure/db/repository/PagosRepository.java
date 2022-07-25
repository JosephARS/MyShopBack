package com.payu.myshop.transactionalms.infrastructure.db.repository;

import com.payu.myshop.transactionalms.infrastructure.db.entities.PagosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagosRepository extends JpaRepository<PagosEntity, Long> {
}
