package com.payu.myshop.transactionalms.infrastructure.db.persistance;

import com.payu.myshop.transactionalms.domain.models.dto.Pago;
import com.payu.myshop.transactionalms.domain.ports.repositories.PagosRepositoryPort;
import com.payu.myshop.transactionalms.infrastructure.db.entities.PagosEntity;
import com.payu.myshop.transactionalms.infrastructure.db.repository.PagosRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("pagosRepositoryPort")
public class PagosPersistance implements PagosRepositoryPort {

    PagosRepository pagosRepository;

    public PagosPersistance(PagosRepository pagosRepository) {
        this.pagosRepository = pagosRepository;
    }

    @Override
    public Pago savePago(Pago pago) {

        return pagosRepository.save(new PagosEntity(pago)).toDto();
    }

    @Override
    public Pago findById(Long idPago) {
        return pagosRepository.findById(idPago).orElseThrow().toDto();
    }

    @Override
    public void updatePago(Pago pago) {
        Optional<PagosEntity> pagoToUpdate = pagosRepository.findById(pago.getIdPago());
        pagosRepository.save(pagoToUpdate.get());
    }
}
