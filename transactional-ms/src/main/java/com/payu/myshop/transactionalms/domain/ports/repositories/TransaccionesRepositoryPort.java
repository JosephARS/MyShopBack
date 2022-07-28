package com.payu.myshop.transactionalms.domain.ports.repositories;

import com.payu.myshop.transactionalms.domain.models.dto.Pago;
import com.payu.myshop.transactionalms.domain.models.dto.Transaccion;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionesRepositoryPort {

    Transaccion saveTransaccion(Transaccion transaccion);

    List<Transaccion> findByPago(Long pago);
}
