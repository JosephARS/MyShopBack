package com.payu.myshop.transactionalms.domain.usecases;

import com.payu.myshop.transactionalms.domain.models.dto.Pago;
import com.payu.myshop.transactionalms.domain.models.dto.RequestCreateTokenPayu;
import com.payu.myshop.transactionalms.domain.models.dto.RequestPaymentPayu;
import com.payu.myshop.transactionalms.domain.models.dto.RequestRefundPayu;
import com.payu.myshop.transactionalms.domain.models.dto.ResponseCreateTokenPayu;
import com.payu.myshop.transactionalms.domain.models.dto.ResponsePaymentPayu;
import com.payu.myshop.transactionalms.domain.models.dto.State;
import com.payu.myshop.transactionalms.domain.models.dto.TipoRespuesta;
import com.payu.myshop.transactionalms.domain.models.dto.Transaccion;
import com.payu.myshop.transactionalms.domain.models.endpoints.AuthorizePaymentRequest;
import com.payu.myshop.transactionalms.domain.models.endpoints.RefundRequest;
import com.payu.myshop.transactionalms.domain.models.endpoints.ResponseWsTransactional;
import com.payu.myshop.transactionalms.domain.ports.repositories.PagosRepositoryPort;
import com.payu.myshop.transactionalms.domain.ports.repositories.TransaccionesRepositoryPort;
import com.payu.myshop.transactionalms.domain.ports.services.TransactionalService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@NoArgsConstructor
public class TransactionalServiceImpl implements TransactionalService {



    public TransactionalServiceImpl(PagosRepositoryPort pagosRepository, TransaccionesRepositoryPort transaccionesRepository, RestTemplate restTemplate, String URL_STRING, SampleFactory sampleFactory) {
        this.sampleFactory = sampleFactory;
        this.pagosRepository = pagosRepository;
        this.transaccionesRepository = transaccionesRepository;
        this.restTemplate = restTemplate;
        this.URL_STRING = URL_STRING;
    }
    @Autowired
    SampleFactory sampleFactory;
    @Autowired
    PagosRepositoryPort pagosRepository;
    @Autowired
    TransaccionesRepositoryPort transaccionesRepository;

    RestTemplate restTemplate = new RestTemplate();
    @Autowired
    @Value("${payuAPI.url}")
    String URL_STRING;


    @Override
    @CircuitBreaker(name = "paymentService", fallbackMethod = "authorizePaymentFallback")
    @Retry(name = "paymentService", fallbackMethod = "authorizePaymentFallback")
    public ResponseWsTransactional authorizePayment(AuthorizePaymentRequest request) {

        ResponseWsTransactional oResponseWsTransactional = new ResponseWsTransactional();

            RequestPaymentPayu requestPaymentPayu = sampleFactory.requestPaymentPayu(request);

            HttpEntity<RequestPaymentPayu> httpEntity = new HttpEntity<>(requestPaymentPayu);
            ResponseEntity<ResponsePaymentPayu> result = restTemplate.postForEntity(URL_STRING, httpEntity, ResponsePaymentPayu.class);

            log.info("Response ws: AuthorizePaymentRequest: " + " dniBuyer: " + request.getBuyer().getDniNumber()  + result.getBody());

            Pago pago = pagosRepository.savePago(sampleFactory.pagoFactory(request, Objects.requireNonNull(result.getBody())));

            transaccionesRepository.saveTransaccion(sampleFactory.transaccionFactory(pago, result.getBody()));

            oResponseWsTransactional.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWsTransactional.setResultado(sampleFactory.authorizePaymentResponse(pago, result.getBody(), null,null));

        return oResponseWsTransactional;
    }

    public ResponseWsTransactional authorizePaymentFallback(Exception e){
        ResponseWsTransactional oResponseWsTransactional = new ResponseWsTransactional();
        oResponseWsTransactional.setTipoRespuesta(TipoRespuesta.Error);
        oResponseWsTransactional.setMensaje("Service error " + e.getMessage());
        log.error("Error authorizing payment: " +  "fallbackMethod" + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        return oResponseWsTransactional;
    }

    @Override
    @CircuitBreaker(name = "refundService", fallbackMethod = "refundPaymentFallback")
    @Retry(name = "refundService", fallbackMethod = "refundPaymentFallback")
    public ResponseWsTransactional refundPayment(RefundRequest request) {

        ResponseWsTransactional oResponseWsTransactional = new ResponseWsTransactional();

            Pago pagoToUpdate = pagosRepository.findById(request.getPagoId());
            List<Transaccion> transaccionesList = transaccionesRepository.findByPago(request.getPagoId());

            Optional<Transaccion> oTransaccion = transaccionesList.stream()
                    .filter(t -> t.getTransactionType().equalsIgnoreCase("AUTHORIZATION_AND_CAPTURE"))
                    .findFirst();

            RequestRefundPayu requestRefundPayu = sampleFactory.requestRefundPayu(pagoToUpdate,request,oTransaccion.get());

            HttpEntity<RequestRefundPayu> httpEntity = new HttpEntity<>(requestRefundPayu);
            ResponseEntity<ResponsePaymentPayu> result = restTemplate.postForEntity(URL_STRING, httpEntity, ResponsePaymentPayu.class);
            log.info("Response ws: RefundRequest: " + " pagoId: " + request.getPagoId() + result.getBody().toString());

            transaccionesRepository.saveTransaccion(sampleFactory.transaccionFactory(pagoToUpdate, result.getBody()));

            pagoToUpdate.setEstadoPago(State.REFUND.toString());

            pagosRepository.updatePago(pagoToUpdate);

            oResponseWsTransactional.setTipoRespuesta(TipoRespuesta.Exito);


        return oResponseWsTransactional;
    }

    public ResponseWsTransactional refundPaymentFallback(Exception e){
        ResponseWsTransactional oResponseWsTransactional = new ResponseWsTransactional();
        oResponseWsTransactional.setTipoRespuesta(TipoRespuesta.Error);
        oResponseWsTransactional.setMensaje("Service error " + e.getMessage());
        log.error("Error RefundRequest: " +  "refundPaymentFallback: " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        return oResponseWsTransactional;
    }

    @Override
    @CircuitBreaker(name = "paymentTokenService", fallbackMethod = "authorizePaymentTokenFallback")
    @Retry(name = "paymentTokenService", fallbackMethod = "authorizePaymentTokenFallback")
    public ResponseWsTransactional authorizePaymentWithToken(AuthorizePaymentRequest request) {

        ResponseWsTransactional oResponseWsTransactional = new ResponseWsTransactional();
        String creditCardTokenId = null;
        String maskedCardNumber = null;

            if (request.getCreditCardTokenId() == null){

                RequestCreateTokenPayu requestCreateTokenPayu = sampleFactory.requestCreateTokenPayu(request);

                HttpEntity<RequestCreateTokenPayu> httpEntity = new HttpEntity<>(requestCreateTokenPayu);
                ResponseEntity<ResponseCreateTokenPayu> result = restTemplate.postForEntity(URL_STRING, httpEntity, ResponseCreateTokenPayu.class);

                log.info("Response ws: CreateTokenPayu: " + "dniBuyer:" + request.getBuyer().getDniNumber()  + result.getBody().toString());

                creditCardTokenId = result.getBody().getCreditCardToken().getCreditCardTokenId();
                maskedCardNumber = result.getBody().getCreditCardToken().getMaskedNumber();

                request.setCreditCardTokenId(creditCardTokenId);

            }


            RequestPaymentPayu requestPaymentWithTokenPayu = sampleFactory.requestPaymentWithTokenPayu(request);

            HttpEntity<RequestPaymentPayu> httpEntity = new HttpEntity<>(requestPaymentWithTokenPayu);
            ResponseEntity<ResponsePaymentPayu> result = restTemplate.postForEntity(URL_STRING, httpEntity, ResponsePaymentPayu.class);
            log.info("Response ws: AuthorizePaymentWithToken: " + " dniBuyer: " + request.getBuyer().getDniNumber()  + result.getBody().toString());

            Pago pago = pagosRepository.savePago(sampleFactory.pagoFactory(request, result.getBody()));

            transaccionesRepository.saveTransaccion(sampleFactory.transaccionFactory(pago, result.getBody()));

            oResponseWsTransactional.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWsTransactional.setResultado(sampleFactory.authorizePaymentResponse(pago, result.getBody(), creditCardTokenId, maskedCardNumber));


        return oResponseWsTransactional;
    }

    public ResponseWsTransactional authorizePaymentTokenFallback(Exception e){
        ResponseWsTransactional oResponseWsTransactional = new ResponseWsTransactional();
        oResponseWsTransactional.setTipoRespuesta(TipoRespuesta.Error);
        oResponseWsTransactional.setMensaje("Service error " + e.getMessage());
        log.error("Error AuthorizePaymentWithToken: " +  "authorizePaymentTokenFallback: " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        return oResponseWsTransactional;
    }



}
