package com.payu.myshop.transactionalms.domain.usecases;

import com.payu.myshop.transactionalms.domain.models.dto.*;
import com.payu.myshop.transactionalms.domain.models.endpoints.AuthorizePaymentRequest;
import com.payu.myshop.transactionalms.domain.models.endpoints.AuthorizePaymentResponse;
import com.payu.myshop.transactionalms.domain.models.endpoints.RefundRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SampleFactory {

    @Autowired
    private Environment env;

    public RequestPaymentPayu requestPaymentPayu(AuthorizePaymentRequest request){

        return RequestPaymentPayu.builder()
                    .language(env.getProperty("payuAPI.language"))
                    .command(env.getProperty("payuAPI.command"))
                    .merchant(merchantReq())
                    .transaction(transactionReq(request, false))
                    .test(Boolean.valueOf("payuAPI.test"))
                .build();
    }

    public RequestPaymentPayu requestPaymentWithTokenPayu(AuthorizePaymentRequest request) {
        return RequestPaymentPayu.builder()
                .language(env.getProperty("payuAPI.language"))
                .command(env.getProperty("payuAPI.command"))
                .merchant(merchantReq())
                .transaction(transactionReq(request, true))
                .test(Boolean.valueOf("payuAPI.test"))
                .build();
    }

    public AuthorizePaymentResponse authorizePaymentResponse(Pago pago, ResponsePaymentPayu result, String creditCardTokenId, String maskedCardNumber){
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

    public RequestRefundPayu requestRefundPayu(Pago pagoToUpdate, RefundRequest request, Transaccion oTransaccion){
        return RequestRefundPayu.builder()
                .language(env.getProperty("payuAPI.language"))
                .command(env.getProperty("payuAPI.command"))
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

    public RequestCreateTokenPayu requestCreateTokenPayu(AuthorizePaymentRequest request){
        return RequestCreateTokenPayu.builder()
                    .language(env.getProperty("payuAPI.language"))
                    .command(env.getProperty("payuAPI.createToken.command"))
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
                .apiKey(env.getProperty("payuAPI.apiKey"))
                .apiLogin(env.getProperty("payuAPI.apiLogin"))
                .build();
    }

    TransactionReq transactionReq(AuthorizePaymentRequest request, Boolean withToken){

        TransactionReq transaction = TransactionReq.builder()
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
                //.creditCard(request.getCreditCard())
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

}
