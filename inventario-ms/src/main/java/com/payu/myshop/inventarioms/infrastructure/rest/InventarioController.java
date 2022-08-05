package com.payu.myshop.inventarioms.infrastructure.rest;

import com.payu.myshop.inventarioms.domain.models.dto.Inventario;
import com.payu.myshop.inventarioms.domain.models.endpoints.ResponseWsInventario;
import com.payu.myshop.inventarioms.domain.ports.services.InventarioService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/inventario")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class InventarioController {

    InventarioService inventarioService;

    @GetMapping("/")
    public ResponseWsInventario getProductList(){
        return inventarioService.getProductList();
    }

    @GetMapping("/{idInventario}")
    public ResponseWsInventario getProductById(@PathVariable Long idInventario){
        return inventarioService.getProductById(idInventario);
    }

    @PostMapping(path = "/")
    public ResponseWsInventario createProduct(@Valid @RequestBody Inventario request){

        return inventarioService.createProduct(request);
    }

    @DeleteMapping(path = "/{idInventario}")
    public ResponseWsInventario deleteProduct(@PathVariable Long idInventario){

        return inventarioService.deleteProduct(idInventario);
    }

    @PutMapping(path = "/")
    public ResponseWsInventario updateProduct(@Valid @RequestBody Inventario request){
        return inventarioService.updateProduct(request);
    }

    @PutMapping(path = "/stock/{accion}/{idVenta}")
    public ResponseWsInventario updateStock(@Valid @RequestBody List<Inventario> listaProductos,
                                            @PathVariable String accion,
                                            @PathVariable Long idVenta){
        return inventarioService.updateStock(listaProductos, accion, idVenta);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
