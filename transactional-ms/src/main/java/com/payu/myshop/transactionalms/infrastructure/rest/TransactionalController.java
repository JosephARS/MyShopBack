package com.payu.myshop.transactionalms.infrastructure.rest;

import com.payu.myshop.transactionalms.domain.models.endpoints.AuthorizePaymentRequest;
import com.payu.myshop.transactionalms.domain.models.endpoints.RefundRequest;
import com.payu.myshop.transactionalms.domain.models.endpoints.ResponseWsTransactional;
import com.payu.myshop.transactionalms.domain.ports.services.TransactionalService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/transactional")

@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class TransactionalController {

    public TransactionalController(TransactionalService transactionalService) {
        this.transactionalService = transactionalService;
    }

    TransactionalService transactionalService;

    @PostMapping(path = "/payment")
    public ResponseWsTransactional authorizePayment(@Valid @RequestBody AuthorizePaymentRequest request,
                                                    @RequestParam(value = "withToken") Boolean withToken){
        if (withToken) {
            return transactionalService.authorizePaymentWithToken(request);
        }else {
            return transactionalService.authorizePayment(request);
        }
    }
    @PostMapping(path = "/refund")
    public ResponseWsTransactional refundPayment(@Valid @RequestBody RefundRequest request){
        return transactionalService.refundPayment(request);
    }

}
