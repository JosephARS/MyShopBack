package com.payu.myshop.transactionalms.infrastructure.rest;

import com.payu.myshop.transactionalms.domain.models.endpoints.AuthorizePaymentRequest;
import com.payu.myshop.transactionalms.domain.models.endpoints.RefundRequest;
import com.payu.myshop.transactionalms.domain.models.endpoints.ResponseWS;
import com.payu.myshop.transactionalms.domain.ports.services.TransactionalService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/payment")

@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class TransactionalController {

    public TransactionalController(TransactionalService transactionalService) {
        this.transactionalService = transactionalService;
    }

    TransactionalService transactionalService;

    @PostMapping(path = "/")
    public ResponseWS authorizePayment(@Valid @RequestBody AuthorizePaymentRequest request,
                                       @RequestParam(value = "withToken") Boolean withToken){
        if (withToken) {
            return transactionalService.authorizePaymentWithToken(request);
        }else {
            return transactionalService.authorizePayment(request);
        }
    }
    @PostMapping(path = "/refund")
    public ResponseWS refundPayment(@Valid @RequestBody RefundRequest request){
        return transactionalService.refundPayment(request);
    }

}
