package com.payu.myshop.transactionalms.domain.usecases;

import com.payu.myshop.transactionalms.domain.models.dto.AdditionalValues;
import com.payu.myshop.transactionalms.domain.models.dto.Buyer;
import com.payu.myshop.transactionalms.domain.models.dto.CreditCard;
import com.payu.myshop.transactionalms.domain.models.dto.CreditCardToken;
import com.payu.myshop.transactionalms.domain.models.dto.ExtraParameters;
import com.payu.myshop.transactionalms.domain.models.dto.MerchantReq;
import com.payu.myshop.transactionalms.domain.models.dto.Order;
import com.payu.myshop.transactionalms.domain.models.dto.OrderRefund;
import com.payu.myshop.transactionalms.domain.models.dto.Pago;
import com.payu.myshop.transactionalms.domain.models.dto.RequestCreateTokenPayu;
import com.payu.myshop.transactionalms.domain.models.dto.RequestPaymentPayu;
import com.payu.myshop.transactionalms.domain.models.dto.RequestRefundPayu;
import com.payu.myshop.transactionalms.domain.models.dto.ResponsePaymentPayu;
import com.payu.myshop.transactionalms.domain.models.dto.TX_dto;
import com.payu.myshop.transactionalms.domain.models.dto.ThreeDomainSecure;
import com.payu.myshop.transactionalms.domain.models.dto.Transaccion;
import com.payu.myshop.transactionalms.domain.models.dto.TransactionRefundReq;
import com.payu.myshop.transactionalms.domain.models.dto.TransactionReq;
import com.payu.myshop.transactionalms.domain.models.endpoints.AuthorizePaymentRequest;
import com.payu.myshop.transactionalms.domain.models.endpoints.AuthorizePaymentResponse;
import com.payu.myshop.transactionalms.domain.models.endpoints.RefundRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SampleFactory {

    @Value("${payuAPI.language}")
    private  String language;

    @Value("${payuAPI.command}")
    private  String command;
    @Value("${payuAPI.test}")
    private  String test;
    @Value("${payuAPI.apiKey}")
    private  String apiKey;
    @Value("${payuAPI.apiLogin}")
    private  String apiLogin;

    @Value("${payuAPI.accountId}")
    private  String accountId;

    @Value("${payuAPI.referenceCode}")
    private  String referenceCode;

    @Value("${payuAPI.description}")
    private  String description;

    @Value("${payuAPI.notifyUrl}")
    private  String notifyUrl;

    @Value("${payuAPI.installmentsNumber}")
    private  String installmentsNumber;
    @Value("${payuAPI.type}")
    private  String type;
    @Value("${payuAPI.deviceSessionId}")
    private  String deviceSessionId;
    @Value("${payuAPI.ipAddress}")
    private  String ipAddress;
    @Value("${payuAPI.cookie}")
    private  String cookie;
    @Value("${payuAPI.embedded}")
    private  String embedded;
    @Value("${payuAPI.eci}")
    private  String eci;
    @Value("${payuAPI.cavv}")
    private  String cavv;
    @Value("${payuAPI.xid}")
    private  String xid;
    @Value("${payuAPI.directoryServerTransactionId}")
    private  String directoryServerTransactionId;
    @Value("${payuAPI.userAgent}")
    private  String userAgent;


    public RequestPaymentPayu requestPaymentPayu(AuthorizePaymentRequest request){

        return RequestPaymentPayu.builder()
                    .language(language)
                    .command(command)
                    .merchant(merchantReq())
                    .transaction(transactionReq(request, false))
                    .test(Boolean.valueOf(test))
                .build();
    }

    public  RequestPaymentPayu requestPaymentWithTokenPayu(AuthorizePaymentRequest request) {
        return RequestPaymentPayu.builder()
                .language(language)
                .command(command)
                .merchant(merchantReq())
                .transaction(transactionReq(request, true))
                .test(Boolean.valueOf(test))
                .build();
    }

    public  AuthorizePaymentResponse authorizePaymentResponse(Pago pago, ResponsePaymentPayu result, String creditCardTokenId, String maskedCardNumber){
        AuthorizePaymentResponse oAuthorizePayment = AuthorizePaymentResponse.builder()
                .pagoId(pago.getIdPago())
                .orderId(result.getTransactionResponse().getOrderId())
                .transactionId(result.getTransactionResponse().getTransactionId())
                .state(result.getTransactionResponse().getState())
                .responseCode(result.getTransactionResponse().getResponseCode())
                .networkResponseCode(result.getTransactionResponse().getPaymentNetworkResponseCode())
                .authorizationCode(result.getTransactionResponse().getAuthorizationCode())
                .responseMessage(result.getTransactionResponse().getResponseMessage())
                .errorCode(result.getTransactionResponse().getErrorCode())
                .operationDate(result.getTransactionResponse().getOperationDate())
                .build();

        if (creditCardTokenId != null){
            oAuthorizePayment.setCreditCardTokenId(creditCardTokenId);
            oAuthorizePayment.setMaskedCardNumber(maskedCardNumber);
        }

        return oAuthorizePayment;
    }

    public  RequestRefundPayu requestRefundPayu(Pago pagoToUpdate, RefundRequest request, Transaccion oTransaccion){
        return RequestRefundPayu.builder()
                .language(language)
                .command(command)
                .merchant(merchantReq())
                .transaction(TransactionRefundReq.builder()
                        .order(OrderRefund.builder()
                                .id(pagoToUpdate.getPayuOrdenId().toString())
                                .build())
                        .type("REFUND")
                        .reason(request.getObservacion())
                        .parentTransactionId(oTransaccion.getPayuTransaccionId())
                        .build())
                .test(false)
                .build();
    }

    public  RequestCreateTokenPayu requestCreateTokenPayu(AuthorizePaymentRequest request){
        return RequestCreateTokenPayu.builder()
                    .language(language)
                    .command("CREATE_TOKEN")
                    .merchant(merchantReq())
                    .creditCardToken(CreditCardToken.builder()
                                .payerId("1")
                                .name("APPROVED")
                                .identificationNumber(request.getPayer().getDniNumber())
                                .paymentMethod(request.getPaymentMethod())
                                .number(request.getCreditCard().getNumber())
                                .expirationDate(request.getCreditCard().getExpirationDate())
                            .build())
                .build();
    }


     MerchantReq merchantReq(){
        return MerchantReq.builder()
                .apiKey(apiKey)
                .apiLogin(apiLogin)
                .build();
    }

     TransactionReq transactionReq(AuthorizePaymentRequest request, Boolean withToken){

        TransactionReq transaction = TransactionReq.builder()
                .order(Order.builder()
                        .accountId(accountId)
                        .referenceCode(referenceCode + new Date())
                        .description(description)
                        .language(language)
                        .notifyUrl(notifyUrl)
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
                //.creditCard(request.getCreditCard())
                .extraParameters(ExtraParameters.builder()
                        .INSTALLMENTS_NUMBER(installmentsNumber)
                        .build())
                .type(type)
                .paymentMethod(request.getPaymentMethod())
                .paymentCountry(request.getPaymentCountry())
                .deviceSessionId(deviceSessionId)
                .ipAddress(ipAddress)
                .cookie(cookie)
                .userAgent(userAgent)
                .threeDomainSecure(ThreeDomainSecure.builder()
                        .embedded(Boolean.valueOf(embedded))
                        .eci(eci)
                        .cavv(cavv)
                        .xid(xid)
                        .directoryServerTransactionId(directoryServerTransactionId)
                        .build())
                .build();

        if (withToken){
            transaction.setCreditCardTokenId(request.getCreditCardTokenId());
            transaction.setCreditCard(CreditCard.builder()
                        .securityCode(request.getCreditCard().getSecurityCode())
                    .build());
            transaction.setThreeDomainSecure(null);

        }else{
            transaction.setCreditCard(request.getCreditCard());
        }

        return transaction;
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
