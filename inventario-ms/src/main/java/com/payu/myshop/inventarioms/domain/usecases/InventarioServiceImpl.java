package com.payu.myshop.inventarioms.domain.usecases;

import com.payu.myshop.inventarioms.domain.models.dto.TipoRespuesta;
import com.payu.myshop.inventarioms.domain.models.dto.Inventario;
import com.payu.myshop.inventarioms.domain.models.dto.Venta;
import com.payu.myshop.inventarioms.domain.models.endpoints.ProductDetailResponse;
import com.payu.myshop.inventarioms.domain.models.endpoints.ResponseWsInventario;
import com.payu.myshop.inventarioms.domain.ports.repositories.InventarioRepositoryPort;
import com.payu.myshop.inventarioms.domain.ports.services.InventarioService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    public ResponseWsInventario createProduct(Inventario request) {

        ResponseWsInventario oResponseWsInventario = new ResponseWsInventario();

        try{
            Inventario inventario = inventarioRepository.save(request);

            oResponseWsInventario.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWsInventario.setResultado(inventario);


        } catch (Exception e){
            oResponseWsInventario.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWsInventario.setMensaje( e.getMessage() );
            log.error("Error creando nuevo producto " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }

        return oResponseWsInventario;
    }

    @Override

    public ResponseWsInventario getProductList() {

        ResponseWsInventario oResponseWsInventario = new ResponseWsInventario();
        List<Object> oProductList = new ArrayList<Object>();

        try {
            List<Inventario> inventarioList = inventarioRepository.findAll();

            oProductList = inventarioList.stream().filter(product -> product.getActivo()).collect(Collectors.toList());

            log.info(String.valueOf(oProductList));

            oResponseWsInventario.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWsInventario.setListaResultado(oProductList);
        } catch (Exception e){
            oResponseWsInventario.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWsInventario.setMensaje( e.getMessage() );
            log.error("Error consultando productos " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }
        return oResponseWsInventario;
    }


    @Override
    public ResponseWsInventario getProductById(Long idInventario) {

        ResponseWsInventario oResponseWsInventario = new ResponseWsInventario();

        try {
            Inventario product = inventarioRepository.findById(idInventario);

            oResponseWsInventario.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWsInventario.setResultado(ProductDetailResponse.builder()
                    .idInventario(product.getIdInventario())
                    .nombre(product.getNombre())
                    .descripcion(product.getDescripcion())
                    .imgUrl(product.getImgUrl())
                    .precio(product.getPrecio())
                    .stock(product.getStock())
                    .build());
        } catch (Exception e){
            oResponseWsInventario.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWsInventario.setMensaje( e.getMessage() );
            log.error("Error consultando productos " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }
        return oResponseWsInventario;
    }

    @Override
    public ResponseWsInventario updateProduct(Inventario request) {

        ResponseWsInventario oResponseWsInventario = new ResponseWsInventario();

        try{
            Inventario inventario = inventarioRepository.updateProduct(request);

            oResponseWsInventario.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWsInventario.setResultado(inventario);


        } catch (Exception e){
            oResponseWsInventario.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWsInventario.setMensaje( e.getMessage() );
            log.error("Error actualizando producto " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }

        return oResponseWsInventario;
    }

    @Override
    public ResponseWsInventario deleteProduct(Long idInventario) {

        ResponseWsInventario oResponseWsInventario = new ResponseWsInventario();

        try{

            Inventario inventario = inventarioRepository.deleteProduct(idInventario);

            oResponseWsInventario.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWsInventario.setResultado(inventario);

        } catch (Exception e){
            oResponseWsInventario.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWsInventario.setMensaje( e.getMessage() );
            log.error("Error eliminando producto " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }

        return oResponseWsInventario;
    }

    @Override
    public ResponseWsInventario updateStock(List<Inventario> listaProductos, String accion, Long idVenta) {

        ResponseWsInventario oResponseWsInventario = new ResponseWsInventario();

        try{
        //    throw new RuntimeException("Error");
            inventarioRepository.updateStock(listaProductos, accion);

            oResponseWsInventario.setTipoRespuesta(TipoRespuesta.Exito);

        } catch (Exception e){
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8000/ventas/";
            Venta venta = Venta.builder().idVenta(idVenta).build();
            HttpEntity<Venta> httpEntity = new HttpEntity<>(venta);
            Object result = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, ResponseWsInventario.class);

            oResponseWsInventario.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWsInventario.setMensaje( e.getMessage() );
            log.error("Error actualizando producto " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }

        return oResponseWsInventario;
    }
}
