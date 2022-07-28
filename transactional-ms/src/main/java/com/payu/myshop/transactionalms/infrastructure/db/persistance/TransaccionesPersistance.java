package com.payu.myshop.transactionalms.infrastructure.db.persistance;

import com.payu.myshop.transactionalms.domain.models.dto.Pago;
import com.payu.myshop.transactionalms.domain.models.dto.Transaccion;
import com.payu.myshop.transactionalms.domain.ports.repositories.TransaccionesRepositoryPort;
import com.payu.myshop.transactionalms.infrastructure.db.entities.PagosEntity;
import com.payu.myshop.transactionalms.infrastructure.db.entities.TransaccionesEntity;
import com.payu.myshop.transactionalms.infrastructure.db.repository.TransaccionesRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository("transaccionesRepositoryPort")
public class TransaccionesPersistance implements TransaccionesRepositoryPort{

    TransaccionesRepository transaccionesRepository;

    public TransaccionesPersistance(TransaccionesRepository transaccionesRepository) {
        this.transaccionesRepository = transaccionesRepository;
    }

    @Override
    public Transaccion saveTransaccion(Transaccion transaccion) {
        return transaccionesRepository.save(new TransaccionesEntity(transaccion)).toDto();
    }

    @Override
    public List<Transaccion> findByPago(Long  idPago) {
        return transaccionesRepository.findByPago(PagosEntity.builder().idPago(idPago).build())
                .stream()
                .map(TransaccionesEntity::toDto).collect(Collectors.toList());
    }
}
