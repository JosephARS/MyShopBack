package com.payu.myshop.inventarioms.domain.usecases;

import com.payu.myshop.inventarioms.domain.models.dto.TipoRespuesta;
import com.payu.myshop.inventarioms.domain.models.endpoints.Inventario;
import com.payu.myshop.inventarioms.domain.models.endpoints.ProductDetailResponse;
import com.payu.myshop.inventarioms.domain.models.endpoints.ResponseWS;
import com.payu.myshop.inventarioms.domain.ports.repositories.InventarioRepositoryPort;
import com.payu.myshop.inventarioms.domain.ports.services.InventarioService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class InventarioServiceImpl implements InventarioService {

    InventarioRepositoryPort inventarioRepository;

    @Override
    public ResponseWS createProduct(Inventario request) {

        ResponseWS oResponseWS = new ResponseWS();
        log.info("Request:" +  request);
        try{
            Inventario inventario = inventarioRepository.save(request);

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
            List<Inventario> inventarioList = inventarioRepository.findAll();

            oProductList = inventarioList.stream().filter(product -> product.getActivo()).collect(Collectors.toList());

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
            Inventario product = inventarioRepository.findById(idInventario);

            oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWS.setResultado(ProductDetailResponse.builder()
                    .idInventario(product.getIdInventario())
                    .nombre(product.getNombre())
                    .descripcion(product.getDescripcion())
                    .imgUrl(product.getImgUrl())
                    .precio(product.getPrecio())
                    .stock(product.getStock())
                    .build());
        } catch (Exception e){
            oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWS.setMensaje( e.getMessage() );
            log.error("Error consultando productos " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }
        return oResponseWS;
    }

    @Override
    public ResponseWS updateProduct(Inventario request) {

        ResponseWS oResponseWS = new ResponseWS();

        try{
            Inventario inventario = inventarioRepository.updateProduct(request);

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

            Inventario inventario = inventarioRepository.deleteProduct(idInventario);

            oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWS.setResultado(inventario);

        } catch (Exception e){
            oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWS.setMensaje( e.getMessage() );
            log.error("Error eliminando producto " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }

        return oResponseWS;
    }

    @Override
    public ResponseWS updateStock(List<Inventario> listaProductos, String accion) {

        ResponseWS oResponseWS = new ResponseWS();

        try{
            inventarioRepository.updateStock(listaProductos, accion);

            oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);

        } catch (Exception e){
            oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWS.setMensaje( e.getMessage() );
            log.error("Error actualizando producto " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }

        return oResponseWS;
    }
}
