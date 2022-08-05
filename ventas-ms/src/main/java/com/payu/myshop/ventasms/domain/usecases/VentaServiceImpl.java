package com.payu.myshop.ventasms.domain.usecases;

import com.payu.myshop.ventasms.domain.models.dto.Card;
import com.payu.myshop.ventasms.domain.models.dto.Cliente;
import com.payu.myshop.ventasms.domain.models.dto.DetalleVenta;
import com.payu.myshop.ventasms.domain.models.dto.Shipping;
import com.payu.myshop.ventasms.domain.models.dto.TipoRespuesta;
import com.payu.myshop.ventasms.domain.models.dto.Venta;
import com.payu.myshop.ventasms.domain.models.endpoints.ClienteDetailResponse;
import com.payu.myshop.ventasms.domain.models.endpoints.ResponseWsVentas;
import com.payu.myshop.ventasms.domain.ports.repositories.VentaRepositoryPort;
import com.payu.myshop.ventasms.domain.ports.services.VentaService;
import com.payu.myshop.ventasms.infrastructure.db.entities.CardEntity;
import com.payu.myshop.ventasms.infrastructure.db.entities.ClienteEntity;
import com.payu.myshop.ventasms.infrastructure.db.entities.ShippingEntity;
import com.payu.myshop.ventasms.infrastructure.db.entities.VentaEntity;
import com.payu.myshop.ventasms.infrastructure.db.repository.CardRepository;
import com.payu.myshop.ventasms.infrastructure.db.repository.ClienteRepository;
import com.payu.myshop.ventasms.infrastructure.db.repository.DetalleVentaRepository;
import com.payu.myshop.ventasms.infrastructure.db.repository.ShippingRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class VentaServiceImpl implements VentaService {

    SampleFactory sampleFactory;
    VentaRepositoryPort ventaRepository;
    DetalleVentaRepository detalleVentaRepository;
    ShippingRepository shippingRepository;
    ClienteRepository clienteRepository;
    CardRepository cardRepository;

    /**
     * @param request - Request object with the Sale data to save
     * @param idToken - Credit card's token (if exists)
     * @param maskedCardNumber - Credit card's number related to token (if exists)
     * @param franquicia - Credit card's type (if exists)
     * @return
     */
    @Override
    @Transactional
    public ResponseWsVentas confirmSale(Venta request, String idToken, String maskedCardNumber, String franquicia) {

        ResponseWsVentas oResponseWsVentas = new ResponseWsVentas();

        try{
            ClienteEntity cliente = saveClient(request.getCliente());
            ShippingEntity shipping = saveShipping(request, cliente);

            request.setCliente(cliente.toDto());
            request.setShipping(shipping.toDto());

            Venta venta = ventaRepository.saveVenta(request);

            request.getListaProductos().forEach(producto -> {
                detalleVentaRepository.save(sampleFactory.detalleVentaFactory(venta, producto));
            });

            if (idToken != null) {
                saveCard(idToken,franquicia,maskedCardNumber,cliente);
            }

            oResponseWsVentas.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWsVentas.setResultado(venta);

        }catch (Exception e){
            oResponseWsVentas.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWsVentas.setMensaje(e.getMessage() );
            log.error("Error confirmando venta " + " | " + "IdVenta:" + request.getIdPago() + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }

        return oResponseWsVentas;
    }

    @Override
    public ResponseWsVentas getSales() {

        ResponseWsVentas oResponseWsVentas = new ResponseWsVentas();
        List<Object> oVentasList = new ArrayList<>();

        try {
           oVentasList = ventaRepository.findAllVentas();

            oResponseWsVentas.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWsVentas.setListaResultado(oVentasList);

        } catch (Exception e){
            oResponseWsVentas.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWsVentas.setMensaje( e.getMessage() );
            log.error("Error consultando ventas " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }
        return oResponseWsVentas;
    }

    @Override
    public ResponseWsVentas refundSale(Venta request) {

        ResponseWsVentas oResponseWsVentas = new ResponseWsVentas();
        Venta ventaToUpdate = new Venta();
        try{
            ventaToUpdate = ventaRepository.updateRefund(request.getIdVenta());

            List<DetalleVenta> oDetalleVentaList = getDetalleVenta(ventaToUpdate);

            oResponseWsVentas.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWsVentas.setListaResultado(Arrays.asList(oDetalleVentaList.toArray()));

        } catch (Exception e){
            oResponseWsVentas.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWsVentas.setMensaje( e.getMessage() );
            log.error("Error anulando venta " + " | " + "IdVenta:" + ventaToUpdate.getIdPago() + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }
        return oResponseWsVentas;
    }

    @Override
    public ResponseWsVentas getUser(String email) {

        ResponseWsVentas oResponseWsVentas = new ResponseWsVentas();
        ClienteDetailResponse client = new ClienteDetailResponse();

        try{
            Optional<ClienteEntity> clienteEntity = clienteRepository.findByEmail(email);

            if (clienteEntity.isPresent()) {
                client = getUserDetail(clienteEntity.get());
            }
            oResponseWsVentas.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWsVentas.setResultado(client);

        } catch (Exception e){
            oResponseWsVentas.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWsVentas.setMensaje( e.getMessage() );
            log.error("Error consultando email " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }

        return oResponseWsVentas;
    }

    public ResponseWsVentas rollbackSale(Venta venta){
        ResponseWsVentas oResponseWsVentas = new ResponseWsVentas();

        try {
            Venta ventaUpdated = ventaRepository.rollabackVenta(venta.getIdVenta());

            oResponseWsVentas.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWsVentas.setResultado(ventaUpdated);
        }catch(Exception e){
            oResponseWsVentas.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWsVentas.setMensaje( e.getMessage() );
            log.error("Error rollingback Sale " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }

        return oResponseWsVentas;
    }

    ClienteEntity saveClient(Cliente oClienteReq){

        ClienteEntity cliente =new ClienteEntity();
        if (oClienteReq.getIdCliente() == null){
            cliente  = clienteRepository.save(sampleFactory.clienteFactory(oClienteReq));
        }else{
            cliente = clienteRepository.findById(oClienteReq.getIdCliente()).get();
        }
        return cliente;
    }

    ShippingEntity saveShipping(Venta venta, ClienteEntity cliente){
        Optional<ShippingEntity> shippingResult = shippingRepository.findByCliente(cliente).stream()
                .filter(oShipping -> oShipping.getDireccion().equalsIgnoreCase(venta.getShipping().getDireccion()))
                .findFirst();

        if (shippingResult.isEmpty()) {
            return shippingRepository.save(sampleFactory.shippingFactory(venta, cliente));
        }
        return shippingResult.get();
    }

    void saveCard(String idToken,String  franquicia, String maskedCardNumber, ClienteEntity cliente){
        List<CardEntity> cardList = cardRepository.findByCliente(cliente);
        Optional<CardEntity> cardResult = cardList.stream()
                .filter(card -> card.getCreditCard().equalsIgnoreCase(maskedCardNumber))
                .findFirst();

        if (cardResult.isEmpty()){
            cardRepository.save(sampleFactory.cardFactory(idToken, franquicia,maskedCardNumber,cliente));
        }
    }

    List<DetalleVenta> getDetalleVenta(Venta ventaToUpdate){

        return detalleVentaRepository.findByVenta(new VentaEntity(ventaToUpdate)).stream()
                .map(p -> DetalleVenta.builder()
                        .idDetalleVenta(p.getIdDetalleVenta())
                        .cantidadCompra(p.getCantidad())
                        .precio(p.getPrecio())
                        .idInventario(p.getIdInventario())
                        .build())
                .collect(Collectors.toList());
    }

    ClienteDetailResponse getUserDetail(ClienteEntity clienteEntity){

        Optional<Shipping> shipping = shippingRepository.findByCliente(clienteEntity).stream().map(ShippingEntity::toDto).findFirst();
        List<Card> cardList = cardRepository.findByCliente(clienteEntity).stream().map(CardEntity::toDto).collect(Collectors.toList());
        Cliente cliente = clienteEntity.toDto();

        return ClienteDetailResponse.builder()
                .cliente(cliente)
                .shipping(shipping.get())
                .cardList(cardList)
                .build();
    }


}
