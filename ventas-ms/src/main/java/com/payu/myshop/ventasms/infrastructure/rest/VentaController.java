package com.payu.myshop.ventasms.infrastructure.rest;

import com.payu.myshop.ventasms.domain.models.dto.Venta;
import com.payu.myshop.ventasms.domain.models.endpoints.ResponseWS;
import com.payu.myshop.ventasms.domain.ports.services.VentaService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/ventas")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class VentaController {

    VentaService ventaService;

    @PostMapping(path = "/")
    public ResponseWS confirmarVenta(@Valid @RequestBody Venta request,
                                     @RequestParam(value = "idToken", required=false) String idToken,
                                     @RequestParam(value = "maskedCardNumber", required=false) String maskedCardNumber,
                                     @RequestParam(value = "franquicia", required=false) String franquicia){

        return ventaService.confirmarVenta(request, idToken, maskedCardNumber, franquicia);
    }

    @GetMapping("/")
    public ResponseWS consultarVentas(){
        return ventaService.consultarVentas();
    }

    @PostMapping(path = "/refund/")
    public ResponseWS anularVenta(@Valid @RequestBody Venta request){

        return ventaService.anularVenta(request);
    }

    @GetMapping("/usuario/{email}")
    public ResponseWS consultarUsuarioEmail(@PathVariable String email){
        return ventaService.consultarUsuario(email);
    }
}
