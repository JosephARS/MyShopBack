package com.payu.myshop.inventarioms.infrastructure.rest;

import com.payu.myshop.inventarioms.domain.models.endpoints.ProductRequest;
import com.payu.myshop.inventarioms.domain.models.endpoints.ResponseWS;
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
    public ResponseWS getProductList(){
        return inventarioService.getProductList();
    }

    @GetMapping("/{idInventario}")
    public ResponseWS getProductById(@PathVariable Long idInventario){
        return inventarioService.getProductById(idInventario);
    }

    @PostMapping(path = "/")
    public ResponseWS createProduct(@Valid @RequestBody ProductRequest request){

        return inventarioService.createProduct(request);
    }

    @DeleteMapping(path = "/{idInventario}")
    public ResponseWS deleteProduct(@PathVariable Long idInventario){

        return inventarioService.deleteProduct(idInventario);
    }

    @PutMapping(path = "/")
    public ResponseWS updateProduct(@Valid @RequestBody ProductRequest request){
        return inventarioService.updateProduct(request);
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
