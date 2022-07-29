package com.payu.myshop.transactionalms.domain.usecases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payu.myshop.transactionalms.domain.models.dto.*;
import com.payu.myshop.transactionalms.domain.models.endpoints.AuthorizePaymentRequest;
import com.payu.myshop.transactionalms.domain.models.endpoints.AuthorizePaymentResponse;
import com.payu.myshop.transactionalms.domain.models.endpoints.RefundRequest;
import com.payu.myshop.transactionalms.domain.models.endpoints.ResponseWS;
import com.payu.myshop.transactionalms.domain.ports.repositories.PagosRepositoryPort;
import com.payu.myshop.transactionalms.domain.ports.repositories.TransaccionesRepositoryPort;
import com.payu.myshop.transactionalms.domain.ports.services.TransactionalService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TransactionalServiceImpl implements TransactionalService {

    @Autowired
    private Environment env;

    SampleFactory sampleFactory;
    PagosRepositoryPort pagosRepository;
    TransaccionesRepositoryPort transaccionesRepository;

    @Override
    @CircuitBreaker(name = "paymentService", fallbackMethod = "authorizePaymentFallback")
    @Retry(name = "paymentService", fallbackMethod = "authorizePaymentFallback")
    public ResponseWS authorizePayment(AuthorizePaymentRequest request) {

        ResponseWS oResponseWS = new ResponseWS();
        RestTemplate restTemplate = new RestTemplate();

//        try{

            RequestPaymentPayu requestPaymentPayu = sampleFactory.requestPaymentPayu(request);

            String urlString = env.getProperty("payuAPI.url");
        System.out.println(urlString);
            HttpEntity<RequestPaymentPayu> httpEntity = new HttpEntity<>(requestPaymentPayu);
            ResponseEntity<ResponsePaymentPayu> result = restTemplate.postForEntity(urlString, httpEntity, ResponsePaymentPayu.class);
            log.info("Response ws: AuthorizePaymentRequest: " + " dniBuyer: " + request.getBuyer().getDniNumber()  + result.getBody().toString());

            Pago pago = pagosRepository.savePago(pagoFactory(request, result.getBody()));

            transaccionesRepository.saveTransaccion(transaccionFactory(pago, result.getBody()));

            oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWS.setResultado(sampleFactory.authorizePaymentResponse(pago, result.getBody(), null,null));

//        }catch (Exception e){
//            oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
//            oResponseWS.setMensaje(e.getMessage() );
//            log.error("Error authorizing payment: " +  "dniBuyer: " + request.getBuyer().getDniNumber()+ " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
//        }

        return oResponseWS;
    }

    public ResponseWS authorizePaymentFallback(Exception e){
        ResponseWS oResponseWS = new ResponseWS();
        oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
        oResponseWS.setMensaje("Service offline " + e.getMessage());
      //  log.error("Error authorizing payment: " +  "dniBuyer: " + request.getBuyer().getDniNumber()+ " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        log.error("Error authorizing payment: " +  "dniBuyer: " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        return oResponseWS;
    }

    public ResponseWS authorizePaymentTokenFallback(Exception e){
        ResponseWS oResponseWS = new ResponseWS();
        oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
        oResponseWS.setMensaje("Service offline " + e.getMessage());
        //log.error("Error authorizing payment: " +  "dniBuyer: " + request.getBuyer().getDniNumber()+ " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        return oResponseWS;
    }

    public ResponseWS refundPaymentFallback(Exception e){
        ResponseWS oResponseWS = new ResponseWS();
        oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
        oResponseWS.setMensaje("Service offline " + e.getMessage());
        //log.error("Error authorizing payment: " +  "dniBuyer: " + request.getBuyer().getDniNumber()+ " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        return oResponseWS;
    }

    @Override
    @CircuitBreaker(name = "refundService", fallbackMethod = "refundPaymentFallback")
    @Retry(name = "refundService", fallbackMethod = "refundPaymentFallback")
    public ResponseWS refundPayment(RefundRequest request) {

        ResponseWS oResponseWS = new ResponseWS();
        RestTemplate restTemplate = new RestTemplate();

//        try{

            Pago pagoToUpdate = pagosRepository.findById(request.getPagoId());
            List<Transaccion> transaccionesList = transaccionesRepository.findByPago(request.getPagoId());

            Optional<Transaccion> oTransaccion = transaccionesList.stream()
                    .filter(t -> t.getTransactionType().equalsIgnoreCase("AUTHORIZATION_AND_CAPTURE"))
                    .findFirst();

            RequestRefundPayu requestRefundPayu = sampleFactory.requestRefundPayu(pagoToUpdate,request,oTransaccion.get());

            String urlString = env.getProperty("payuAPI.url");

            HttpEntity<RequestRefundPayu> httpEntity = new HttpEntity<>(requestRefundPayu);
            ResponseEntity<ResponsePaymentPayu> result = restTemplate.postForEntity(urlString, httpEntity, ResponsePaymentPayu.class);
            log.info("Response ws: RefundRequest: " + " pagoId: " + request.getPagoId() + result.getBody().toString());

            transaccionesRepository.saveTransaccion(transaccionFactory(pagoToUpdate, result.getBody()));

            pagoToUpdate.setEstadoPago(State.REFUND.toString());

            pagosRepository.updatePago(pagoToUpdate);

            oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);

//        }catch (Exception e){
//            oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
//            oResponseWS.setMensaje(e.getMessage() );
//            log.error("Error refund payment " + " pagoId: " + request.getPagoId() + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
//        }

        return oResponseWS;
    }

    @Override
    @CircuitBreaker(name = "paymentTokenService", fallbackMethod = "authorizePaymentTokenFallback")
    @Retry(name = "paymentTokenService", fallbackMethod = "authorizePaymentTokenFallback")
    public ResponseWS authorizePaymentWithToken(AuthorizePaymentRequest request) {

        ResponseWS oResponseWS = new ResponseWS();
        RestTemplate restTemplate = new RestTemplate();
        String creditCardTokenId = null;
        String maskedCardNumber = null;

//        try{

            if (request.getCreditCardTokenId() == null){

                RequestCreateTokenPayu requestCreateTokenPayu = sampleFactory.requestCreateTokenPayu(request);

                HttpEntity<RequestCreateTokenPayu> httpEntity = new HttpEntity<>(requestCreateTokenPayu);
                String urlString = env.getProperty("payuAPI.url");
                ResponseEntity<ResponseCreateTokenPayu> result = restTemplate.postForEntity(urlString, httpEntity, ResponseCreateTokenPayu.class);

                if (result != null) {
                    creditCardTokenId = result.getBody().getCreditCardToken().getCreditCardTokenId();
                    maskedCardNumber = result.getBody().getCreditCardToken().getMaskedNumber();
                }
                request.setCreditCardTokenId(creditCardTokenId);
                log.info("Response ws: CreateTokenPayu: " + "dniBuyer:" + request.getBuyer().getDniNumber()  + result.getBody().toString());
            }

            //Pagar con token

            RequestPaymentPayu requestPaymentWithTokenPayu = sampleFactory.requestPaymentWithTokenPayu(request);
            String urlString = env.getProperty("payuAPI.url");

            HttpEntity<RequestPaymentPayu> httpEntity = new HttpEntity<>(requestPaymentWithTokenPayu);
            ResponseEntity<ResponsePaymentPayu> result = restTemplate.postForEntity(urlString, httpEntity, ResponsePaymentPayu.class);
            log.info("Response ws: AuthorizePaymentWithToken: " + " dniBuyer: " + request.getBuyer().getDniNumber()  + result.getBody().toString());

            Pago pago = pagosRepository.savePago(pagoFactory(request, result.getBody()));

            transaccionesRepository.saveTransaccion(transaccionFactory(pago, result.getBody()));

            oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWS.setResultado(sampleFactory.authorizePaymentResponse(pago, result.getBody(), creditCardTokenId, maskedCardNumber));

//        }catch (Exception e){
//            oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
//            oResponseWS.setMensaje(e.getMessage() );
//            log.error("Error authorizing payment: " +  " dniBuyer: " + request.getBuyer().getDniNumber() + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
//        }

        return oResponseWS;
    }


    Pago pagoFactory(AuthorizePaymentRequest request,ResponsePaymentPayu result){
        return Pago.builder()
                .payuOrdenId(result.getTransactionResponse().getOrderId())
                .fecha(new Date())
                .dniPayer(request.getPayer().getDniNumber())
                .dniBuyer(request.getBuyer().getDniNumber())
                .valor(request.getTotalPrice() + request.getTotalTax())
                .direccion(request.getBuyer().getShippingAddress().getStreet1())
                .ciudad(request.getBuyer().getShippingAddress().getCity())
                .pais(request.getBuyer().getShippingAddress().getCountry())
                .metodoPago(result.getTransactionResponse().getAdditionalInfo().getCardType())
                .franquicia(request.getPaymentMethod())
                .estadoPago(result.getTransactionResponse().getState())
            .build();
    }

    Transaccion transaccionFactory(Pago pago,ResponsePaymentPayu result) {
        return Transaccion.builder()
                .payuTransaccionId(result.getTransactionResponse().getTransactionId())
                .fecha(new Date())
                .state(result.getTransactionResponse().getState())
                .codeResponde(result.getTransactionResponse().getResponseCode())
                .networkResponseCode(result.getTransactionResponse().getPaymentNetworkResponseCode())
                .authorizationCode(result.getTransactionResponse().getAuthorizationCode())
                .responseMessage(result.getTransactionResponse().getResponseMessage())
                .rejectionType(result.getTransactionResponse().getAdditionalInfo().getRejectionType())
                .transactionType(result.getTransactionResponse().getAdditionalInfo().getTransactionType())
                .pago(pago)
                .build();
    }


}
