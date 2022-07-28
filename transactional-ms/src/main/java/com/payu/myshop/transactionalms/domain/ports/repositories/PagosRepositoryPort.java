package com.payu.myshop.transactionalms.domain.ports.repositories;

import com.payu.myshop.transactionalms.domain.models.dto.Pago;
import org.springframework.stereotype.Repository;

@Repository
public interface PagosRepositoryPort {

    Pago savePago(Pago pago);

    Pago findById(Long idPago);

    void updatePago(Pago pago);
}
