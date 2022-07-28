package com.payu.myshop.ventasms.domain.usecases;

import com.payu.myshop.ventasms.domain.models.dto.*;
import com.payu.myshop.ventasms.domain.models.endpoints.ClienteDetailResponse;
import com.payu.myshop.ventasms.domain.models.endpoints.ResponseWS;
import com.payu.myshop.ventasms.domain.ports.repositories.VentaRepositoryPort;
import com.payu.myshop.ventasms.domain.ports.services.VentaService;
import com.payu.myshop.ventasms.infrastructure.db.entities.*;
import com.payu.myshop.ventasms.infrastructure.db.repository.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class VentaServiceImpl implements VentaService {

    VentaRepositoryPort ventaRepository;
    DetalleVentaRepository detalleVentaRepository;
    ShippingRepository shippingRepository;
    ClienteRepository clienteRepository;
    CardRepository cardRepository;

    @Override
    @Transactional
    public ResponseWS confirmarVenta(Venta request, String idToken, String maskedCardNumber, String franquicia) {

        ResponseWS oResponseWS = new ResponseWS();
        System.out.println("VentaServiceImpl.confirmarVenta" + idToken + maskedCardNumber + franquicia);
        try{
            ClienteEntity cliente = new ClienteEntity();

            if (request.getCliente().getIdCliente() == null){
                cliente  = clienteRepository.save(ClienteEntity.builder()
                        .nombre(request.getCliente().getNombre())
                        .apellido(request.getCliente().getApellido())
                        .dni(request.getCliente().getDni())
                        .email(request.getCliente().getEmail())
                        .telefono(request.getCliente().getTelefono())
                        .build());
            }else{
                cliente = clienteRepository.findById(request.getCliente().getIdCliente()).get();
            }

            ShippingEntity shipping = shippingRepository.save(ShippingEntity.builder()
                            .direccion(request.getShipping().getDireccion())
                            .ciudad(request.getShipping().getCiudad())
                            .departamento(request.getShipping().getDepartamento())
                            .pais(request.getShipping().getPais())
                            .postalCode(request.getShipping().getPostalCode())
                            .cliente(cliente)
                    .build());

            request.setCliente(cliente.toDto());
            request.setShipping(shipping.toDto());

            Venta venta = ventaRepository.saveVenta(request);

            request.getListaProductos().forEach(producto -> {
                detalleVentaRepository.save(DetalleVentaEntity.builder()
                                .venta(new VentaEntity(venta))
                                .cantidad(producto.getCantidadCompra())
                                .precio(producto.getPrecio())
                                .idInventario(producto.getIdInventario())
                        .build());
            });

            if (idToken != null) {

                List<CardEntity> cardList = cardRepository.findByCliente(cliente);
                Optional<CardEntity> cardResult = cardList.stream().filter(card -> card.getCreditCard().equalsIgnoreCase(maskedCardNumber)).findFirst();
                System.out.println("cardResult = " + cardResult);
                if (cardResult.isEmpty()){
                    cardRepository.save(CardEntity.builder()
                                    .token(idToken)
                                    .franquicia(franquicia)
                                    .creditCard(maskedCardNumber)
                                    .cliente(cliente)
                            .build());
                }
            }

            oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWS.setResultado(venta);

        }catch (Exception e){
            oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWS.setMensaje(e.getMessage() );
            log.error("Error confirmando venta " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }

        return oResponseWS;
    }

    @Override
    public ResponseWS consultarVentas() {

        ResponseWS oResponseWS = new ResponseWS();
        List<Object> oVentasList = new ArrayList<Object>();

        try {
           oVentasList = ventaRepository.findAllVentas();

            oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWS.setListaResultado(oVentasList);

        } catch (Exception e){
            oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWS.setMensaje( e.getMessage() );
            log.error("Error consultando ventas " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }
        return oResponseWS;
    }

    @Override
    public ResponseWS anularVenta(Venta request) {

        ResponseWS oResponseWS = new ResponseWS();

        try{
            Venta ventaToUpdate = ventaRepository.updateRefund(request.getIdVenta());


            List<DetalleVenta> oDetalleVenta = detalleVentaRepository.findByVenta(new VentaEntity(ventaToUpdate)).stream()
                    .map(p -> DetalleVenta.builder()
                            .idDetalleVenta(p.getIdDetalleVenta())
                            .cantidadCompra(p.getCantidad())
                            .precio(p.getPrecio())
                            .idInventario(p.getIdInventario())
                            .build())
                    .collect(Collectors.toList());

            oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWS.setListaResultado(Arrays.asList(oDetalleVenta.toArray()));

        } catch (Exception e){
            oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWS.setMensaje( e.getMessage() );
            log.error("Error anulando venta " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }
        return oResponseWS;
    }

    @Override
    public ResponseWS consultarUsuario(String email) {

        ResponseWS oResponseWS = new ResponseWS();

        try{

            Optional<ClienteEntity> clienteEntity = clienteRepository.findByEmail(email);

            if (clienteEntity.isPresent()){
                Optional<Shipping> shipping = shippingRepository.findByCliente(clienteEntity.get()).stream().map(s -> Shipping.builder()
                        .idShipping(s.getIdShipping())
                        .ciudad(s.getCiudad())
                        .departamento(s.getDepartamento())
                        .direccion(s.getDireccion())
                        .pais(s.getPais())
                        .postalCode(s.getPostalCode())
                        .build()).findFirst();
                List<Card> cardList = cardRepository.findByCliente(clienteEntity.get()).stream().map(c -> Card.builder()
                        .idCard(c.getIdCard())
                        .creditCard(c.getCreditCard())
                        .franquicia(c.getFranquicia())
                        .token(c.getToken())
                        .build()).collect(Collectors.toList());
                Optional<Cliente> cliente = clienteEntity.map(cli -> Cliente.builder()
                        .idCliente(cli.getIdCliente())
                        .apellido(cli.getApellido())
                        .nombre(cli.getNombre())
                        .dni(cli.getDni())
                        .email(cli.getEmail())
                        .telefono(cli.getTelefono())
                        .build());
                oResponseWS.setResultado(ClienteDetailResponse.builder()
                                .cliente(cliente.get())
                                .shipping(shipping.get())
                                .cardList(cardList)
                        .build());
            }

            oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);


        } catch (Exception e){
            oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWS.setMensaje( e.getMessage() );
            log.error("Error consultando email " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }
            return oResponseWS;
        }
}
