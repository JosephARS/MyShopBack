package com.payu.myshop.ventasms.infrastructure.rest;

import com.payu.myshop.ventasms.domain.models.dto.Venta;
import com.payu.myshop.ventasms.domain.models.endpoints.ResponseWsVentas;
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
    public ResponseWsVentas confirmSale(@Valid @RequestBody Venta request,
                                        @RequestParam(value = "idToken", required=false) String idToken,
                                        @RequestParam(value = "maskedCardNumber", required=false) String maskedCardNumber,
                                        @RequestParam(value = "franquicia", required=false) String franquicia){

        return ventaService.confirmSale(request, idToken, maskedCardNumber, franquicia);
    }

    @GetMapping("/")
    public ResponseWsVentas getSales(){
        return ventaService.getSales();
    }

    @PostMapping(path = "/refund/")
    public ResponseWsVentas refundSale(@Valid @RequestBody Venta request){
        return ventaService.refundSale(request);
    }

    @GetMapping("/usuario/{email}")
    public ResponseWsVentas getUserEmail(@PathVariable String email){
        return ventaService.getUser(email);
    }

    @PutMapping("/")
    public ResponseWsVentas rollbackSale(@Valid @RequestBody Venta venta){
        return ventaService.rollbackSale(venta);
    }

}
