package com.payu.myshop.inventarioms.domain.usecases;

import com.payu.myshop.inventarioms.domain.models.endpoints.ProductRequest;
import com.payu.myshop.inventarioms.domain.models.endpoints.ProductDetailResponse;
import com.payu.myshop.inventarioms.domain.models.endpoints.ResponseWS;
import com.payu.myshop.inventarioms.domain.models.endpoints.TipoRespuesta;
import com.payu.myshop.inventarioms.domain.ports.services.InventarioService;
import com.payu.myshop.inventarioms.infrastructure.entities.InventarioEntity;
import com.payu.myshop.inventarioms.domain.ports.repositories.InventarioRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class InventarioServiceImpl implements InventarioService {

    InventarioRepository inventarioRepository;

    @Override
    public ResponseWS createProduct(ProductRequest request) {

        ResponseWS oResponseWS = new ResponseWS();
        log.info("Request:" +  request);
        try{
            InventarioEntity inventario = inventarioRepository.save(InventarioEntity.builder()
                            .nombre(request.getNombre())
                            .descripcion(request.getDescripcion())
                            .imgUrl(request.getImgUrl())
                            .precio(request.getPrecio())
                            .stock(request.getStock())
                            .activo(true)
                    .build());

            oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWS.setResultado(inventario);


        } catch (Exception e){
            oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWS.setMensaje( e.getMessage() );
            log.error("Error creando nuevo producto " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }

        return oResponseWS;
    }

    @Override
    public ResponseWS getProductList() {

        ResponseWS oResponseWS = new ResponseWS();
        List<Object> oProductList = new ArrayList<Object>();

        try {
            List<InventarioEntity> inventarioList = inventarioRepository.findAll();

            oProductList = inventarioList.stream().filter(product -> product.getActivo()).map(product -> ProductDetailResponse.builder()
                    .idInventario(product.getIdInventario())
                    .nombre(product.getNombre())
                    .descripcion(product.getDescripcion())
                    .imgUrl(product.getImgUrl())
                    .precio(product.getPrecio())
                    .stock(product.getStock())
                    .build()).collect(Collectors.toList());

//
//
//            inventarioList.forEach(product ->{
//                oProductList.add(ProductDetailResponse.builder()
//                        .idInventario(product.getIdInventario())
//                        .nombre(product.getNombre())
//                        .descripcion(product.getDescripcion())
//                        .imgUrl(product.getImgUrl())
//                        .precio(product.getPrecio())
//                        .stock(product.getStock())
//                        .build());
//            });


            log.info(String.valueOf(oProductList));

            oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWS.setListaResultado(oProductList);
        } catch (Exception e){
            oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWS.setMensaje( e.getMessage() );
            log.error("Error consultando productos " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }
        return oResponseWS;
    }

    @Override
    public ResponseWS getProductById(Long idInventario) {

        ResponseWS oResponseWS = new ResponseWS();

        try {
            Optional<InventarioEntity> product = inventarioRepository.findById(idInventario);

            oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWS.setResultado(ProductDetailResponse.builder()
                    .idInventario(product.get().getIdInventario())
                    .nombre(product.get().getNombre())
                    .descripcion(product.get().getDescripcion())
                    .imgUrl(product.get().getImgUrl())
                    .precio(product.get().getPrecio())
                    .stock(product.get().getStock())
                    .build());
        } catch (Exception e){
            oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWS.setMensaje( e.getMessage() );
            log.error("Error consultando productos " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }
        return oResponseWS;
    }

    @Override
    public ResponseWS updateProduct(ProductRequest request) {

        ResponseWS oResponseWS = new ResponseWS();

        try{

            Optional<InventarioEntity> productToUpdate = inventarioRepository.findById(request.getIdInventario());
            productToUpdate.get().setIdInventario(request.getIdInventario());
            productToUpdate.get().setNombre(request.getNombre());
            productToUpdate.get().setDescripcion(request.getDescripcion());
            productToUpdate.get().setPrecio(request.getPrecio());
            productToUpdate.get().setStock(request.getStock());
            productToUpdate.get().setImgUrl(request.getImgUrl());
            log.info("Dimelo" + productToUpdate);
            InventarioEntity inventario = inventarioRepository.save(productToUpdate.get());

            oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWS.setResultado(inventario);


        } catch (Exception e){
            oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWS.setMensaje( e.getMessage() );
            log.error("Error actualizando producto " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }

        return oResponseWS;
    }

    @Override
    public ResponseWS deleteProduct(Long idInventario) {

        ResponseWS oResponseWS = new ResponseWS();

        try{
            Optional<InventarioEntity> productToDelete = inventarioRepository.findById(idInventario);
            productToDelete.get().setActivo(false);

            InventarioEntity inventario = inventarioRepository.save(productToDelete.get());

            oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWS.setResultado(inventario);

        } catch (Exception e){
            oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWS.setMensaje( e.getMessage() );
            log.error("Error eliminando producto " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }

        return oResponseWS;
    }
}
