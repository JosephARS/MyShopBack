package com.payu.myshop.transactionalms.domain.usecases;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payu.myshop.transactionalms.domain.models.dto.*;
import com.payu.myshop.transactionalms.domain.models.endpoints.AuthorizePaymentRequest;
import com.payu.myshop.transactionalms.domain.models.endpoints.AuthorizePaymentResponse;
import com.payu.myshop.transactionalms.domain.models.endpoints.RefundRequest;
import com.payu.myshop.transactionalms.domain.models.endpoints.ResponseWS;
import com.payu.myshop.transactionalms.infrastructure.db.repository.PagosRepository;
import com.payu.myshop.transactionalms.infrastructure.db.repository.TransaccionesRepository;
import com.payu.myshop.transactionalms.domain.ports.services.TransactionalService;
import com.payu.myshop.transactionalms.infrastructure.db.entities.PagosEntity;
import com.payu.myshop.transactionalms.infrastructure.db.entities.TransaccionesEntity;
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
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TransactionalServiceImpl implements TransactionalService {

    @Autowired
    private Environment env;
    PagosRepository pagosRepository;
    TransaccionesRepository transaccionesRepository;

    @Override
    public ResponseWS authorizePayment(AuthorizePaymentRequest request) {

        ResponseWS oResponseWS = new ResponseWS();
        RestTemplate restTemplate = new RestTemplate();

        try{

            String signarure_params = env.getProperty("payuAPI.apiKey") + "~" +
                    env.getProperty("payuAPI.merchantId") + "~" +
                    env.getProperty("payuAPI.referenceCode") + "~" +
                    request.getTotalPrice() + "~" +
                    request.getCurrency();

            RequestPaymentPayu requestPaymentPayu = RequestPaymentPayu.builder()
                    .language(env.getProperty("payuAPI.language"))
                    .command(env.getProperty("payuAPI.command"))
                    .merchant(MerchantReq.builder()
                            .apiKey(env.getProperty("payuAPI.apiKey"))
                            .apiLogin(env.getProperty("payuAPI.apiLogin"))
                            .build())
                    .transaction(TransactionReq.builder()
                            .order(Order.builder()
                                    .accountId(env.getProperty("payuAPI.accountId"))
                                    .referenceCode(env.getProperty("payuAPI.referenceCode") + new Date())
                                    .description(env.getProperty("payuAPI.description"))
                                    .language(env.getProperty("payuAPI.language"))
                                    //.signature(createSignature(signarure_params))
                                    .notifyUrl(env.getProperty("payuAPI.notifyUrl"))
                                    .additionalValues(AdditionalValues.builder()
                                            .TX_VALUE(TX_dto.builder()
                                                    .value(request.getTotalPrice())
                                                    .currency(request.getCurrency())
                                                    .build())
                                            .TX_TAX(TX_dto.builder()
                                                    .value(request.getTotalTax())
                                                    .currency(request.getCurrency())
                                                    .build())
                                            .TX_TAX_RETURN_BASE(TX_dto.builder()
                                                    .value(0L)
                                                    .currency(request.getCurrency())
                                                    .build())
                                            .build())
                                    .buyer(Buyer.builder()
                                            .merchantBuyerId(request.getBuyer().getMerchantBuyerId())
                                            .fullName(request.getBuyer().getFullName())
                                            .emailAddress(request.getBuyer().getEmailAddress())
                                            .contactPhone(request.getBuyer().getContactPhone())
                                            .dniNumber(request.getBuyer().getDniNumber())
                                            .shippingAddress(request.getBuyer().getShippingAddress())
                                            .build())
                                    .shippingAddress(request.getBuyer().getShippingAddress())
                                    .build())
                            .payer(request.getPayer())
                            .creditCard(request.getCreditCard())
                            .extraParameters(ExtraParameters.builder()
                                    .INSTALLMENTS_NUMBER(env.getProperty("payuAPI.installmentsNumber"))
                                    .build())
                            .type(env.getProperty("payuAPI.type"))
                            .paymentMethod(request.getPaymentMethod())
                            .paymentCountry(request.getPaymentCountry())
                            .deviceSessionId(env.getProperty("payuAPI.deviceSessionId"))
                            .ipAddress(env.getProperty("payuAPI.ipAddress"))
                            .cookie(env.getProperty("payuAPI.cookie"))
                            .userAgent(env.getProperty("payuAPI.userAgent"))
                            .threeDomainSecure(ThreeDomainSecure.builder()
                                    .embedded(Boolean.valueOf(env.getProperty("payuAPI.embedded")))
                                    .eci(env.getProperty("payuAPI.eci"))
                                    .cavv(env.getProperty("payuAPI.cavv"))
                                    .xid(env.getProperty("payuAPI.xid"))
                                    .directoryServerTransactionId(env.getProperty("payuAPI.directoryServerTransactionId"))
                                    .build())
                            .build())
                    .test(Boolean.valueOf("payuAPI.test"))
                    .build();

            log.info("Requ: " + requestPaymentPayu);
            ObjectMapper mapper = new ObjectMapper();
            //Converting the Object to JSONString
            String jsonString = mapper.writeValueAsString(requestPaymentPayu);
            System.out.println(jsonString);
            String urlString = env.getProperty("payuAPI.url");


            HttpEntity<RequestPaymentPayu> httpEntity = new HttpEntity<>(requestPaymentPayu);
            ResponseEntity<ResponsePaymentPayu> result = restTemplate.exchange(urlString, HttpMethod.POST, httpEntity, ResponsePaymentPayu.class);
            log.info("Resultado: " + result.getBody().toString());
            // Guardar en base de datos
            PagosEntity pago = pagosRepository.save(PagosEntity.builder()
                            .payuOrdenId(result.getBody().getTransactionResponse().getOrderId())
                            .fecha(new Date())
                            .dniPayer(request.getPayer().getDniNumber())
                            .dniBuyer(request.getBuyer().getDniNumber())
                            .valor(request.getTotalPrice() + request.getTotalTax())
                            .direccion(request.getBuyer().getShippingAddress().getStreet1())
                            .ciudad(request.getBuyer().getShippingAddress().getCity())
                            .pais(request.getBuyer().getShippingAddress().getCountry())
                            .metodoPago(result.getBody().getTransactionResponse().getAdditionalInfo().getCardType())
                            .franquicia(request.getPaymentMethod())
                            .estadoPago(result.getBody().getTransactionResponse().getState())
                    .build());

            transaccionesRepository.save(TransaccionesEntity.builder()
                    .payuTransaccionId(result.getBody().getTransactionResponse().getTransactionId())
                    .fecha(new Date())
                    .state(result.getBody().getTransactionResponse().getState())
                    .codeResponde(result.getBody().getTransactionResponse().getResponseCode())
                    .networkResponseCode(result.getBody().getTransactionResponse().getPaymentNetworkResponseCode())
                    .authorizationCode(result.getBody().getTransactionResponse().getAuthorizationCode())
                    .responseMessage(result.getBody().getTransactionResponse().getResponseMessage())
                    .rejectionType(result.getBody().getTransactionResponse().getAdditionalInfo().getRejectionType())
                    .transactionType(result.getBody().getTransactionResponse().getAdditionalInfo().getTransactionType())
                    .pago(pago)
                    .build());

            oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);
            oResponseWS.setResultado(AuthorizePaymentResponse.builder()
                            .pagoId(pago.getIdPago())
                            .orderId(result.getBody().getTransactionResponse().getOrderId())
                            .transactionId(result.getBody().getTransactionResponse().getTransactionId())
                            .state(result.getBody().getTransactionResponse().getState())
                            .responseCode(result.getBody().getTransactionResponse().getResponseCode())
                            .networkResponseCode(result.getBody().getTransactionResponse().getPaymentNetworkResponseCode())
                            .authorizationCode(result.getBody().getTransactionResponse().getAuthorizationCode())
                            .responseMessage(result.getBody().getTransactionResponse().getResponseMessage())
                            .errorCode(result.getBody().getTransactionResponse().getErrorCode())
                            .operationDate(result.getBody().getTransactionResponse().getOperationDate())
                    .build());

        }catch (Exception e){
            oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWS.setMensaje(e.getMessage() );
            log.error("Error validando pago " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }

        return oResponseWS;
    }

    @Override
    public ResponseWS refundPayment(RefundRequest request) {

        ResponseWS oResponseWS = new ResponseWS();
        RestTemplate restTemplate = new RestTemplate();

        try{

            Optional<PagosEntity> pagoToUpdate = pagosRepository.findById(request.getPagoId());
            List<TransaccionesEntity> transaccionesList = transaccionesRepository.findByPago(PagosEntity.builder().idPago(request.getPagoId()).build());

            Optional<TransaccionesEntity> oTransaccion = transaccionesList.stream()
                    .filter(t -> t.getTransactionType().equalsIgnoreCase("AUTHORIZATION_AND_CAPTURE"))
                    .findFirst();

            RequestRefundPayu requestRefundPayu = RequestRefundPayu.builder()
                    .language(env.getProperty("payuAPI.language"))
                    .command(env.getProperty("payuAPI.command"))
                    .merchant(MerchantReq.builder()
                            .apiKey(env.getProperty("payuAPI.apiKey"))
                            .apiLogin(env.getProperty("payuAPI.apiLogin"))
                            .build())
                    .transaction(TransactionRefundReq.builder()
                            .order(OrderRefund.builder()
                                    .id(pagoToUpdate.get().getPayuOrdenId().toString())
                                    .build())
                            .type("REFUND")
                            .reason(request.getObservacion())
                            .parentTransactionId(oTransaccion.get().getPayuTransaccionId())
                            .build())
                    .test(false)
                    .build();

            String urlString = env.getProperty("payuAPI.url");

            HttpEntity<RequestRefundPayu> httpEntity = new HttpEntity<>(requestRefundPayu);
            ResponseEntity<ResponsePaymentPayu> result = restTemplate.exchange(urlString, HttpMethod.POST, httpEntity, ResponsePaymentPayu.class);
            log.info("Resultado: " + result.getBody().toString());



            transaccionesRepository.save(TransaccionesEntity.builder()
                    .payuTransaccionId(result.getBody().getTransactionResponse().getTransactionId())
                    .fecha(new Date())
                    .state(result.getBody().getTransactionResponse().getState())
                    .codeResponde(result.getBody().getTransactionResponse().getResponseCode())
                    .networkResponseCode(result.getBody().getTransactionResponse().getPaymentNetworkResponseCode())
                    .authorizationCode(result.getBody().getTransactionResponse().getAuthorizationCode())
                    .responseMessage(result.getBody().getTransactionResponse().getResponseMessage())
                    .transactionType("REFUND")
                    .pago(pagoToUpdate.get())
                    .build());

            pagoToUpdate.get().setEstadoPago("REFUND");

            pagosRepository.save(pagoToUpdate.get());

            oResponseWS.setTipoRespuesta(TipoRespuesta.Exito);

        }catch (Exception e){
            oResponseWS.setTipoRespuesta(TipoRespuesta.Error);
            oResponseWS.setMensaje(e.getMessage() );
            log.error("Error anulando pago " + " | " + e.getMessage() + " | " + e.getCause() + " | " + e.getStackTrace()[0]);
        }

        return oResponseWS;
    }

    public String createSignature(String parametros) {
        byte[] bytesOfMessage = parametros.getBytes(StandardCharsets.UTF_8);
        String signature = DigestUtils.md5DigestAsHex(bytesOfMessage);
        log.info(signature);
        return signature;
    }

    @Override
    public ResponseWS getPaymentList() {

        return null;
    }

}
