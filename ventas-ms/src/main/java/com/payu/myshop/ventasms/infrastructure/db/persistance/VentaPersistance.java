package com.payu.myshop.ventasms.infrastructure.db.persistance;

import com.payu.myshop.ventasms.domain.models.dto.State;
import com.payu.myshop.ventasms.domain.models.dto.Venta;
import com.payu.myshop.ventasms.domain.ports.repositories.VentaRepositoryPort;
import com.payu.myshop.ventasms.infrastructure.db.entities.VentaEntity;
import com.payu.myshop.ventasms.infrastructure.db.repository.VentaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository("ventaRepositoryPort")
public class VentaPersistance implements VentaRepositoryPort {

    VentaRepository ventaRepository;

    public VentaPersistance(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public Venta saveVenta(Venta venta) {
        venta.setFecha(new Date());
        return ventaRepository.save(new VentaEntity(venta)).toDto();

    }

    @Override
    public List<Object> findAllVentas() {

       return ventaRepository.findAll().stream().map(VentaEntity::toDto).collect(Collectors.toList());

    }

    @Override
    @Transactional
    public Venta updateRefund(Long idVenta) {
        Optional<VentaEntity> ventaToUpdate = ventaRepository.findById(idVenta);
        ventaToUpdate.get().setEstado(State.REFUND.toString());

        return ventaRepository.save(ventaToUpdate.get()).toDto();
    }

    @Override
    public Venta rollabackVenta(Long idVenta) {
        Optional<VentaEntity> ventaToUpdate = ventaRepository.findById(idVenta);
        ventaToUpdate.get().setEstado(State.AUTOCANCELLED.toString());

        return ventaRepository.save(ventaToUpdate.get()).toDto();
    }

}
