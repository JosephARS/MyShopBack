package com.payu.myshop.transactionalms.utils.dto;

import com.payu.myshop.transactionalms.domain.models.dto.AdditionalInfo;
import com.payu.myshop.transactionalms.domain.models.dto.BillingAddress;
import com.payu.myshop.transactionalms.domain.models.dto.Buyer;
import com.payu.myshop.transactionalms.domain.models.dto.CreditCard;
import com.payu.myshop.transactionalms.domain.models.dto.CreditCardToken;
import com.payu.myshop.transactionalms.domain.models.dto.ExtraParametersResp;
import com.payu.myshop.transactionalms.domain.models.dto.Pago;
import com.payu.myshop.transactionalms.domain.models.dto.Payer;
import com.payu.myshop.transactionalms.domain.models.dto.ResponseCreateTokenPayu;
import com.payu.myshop.transactionalms.domain.models.dto.ResponsePaymentPayu;
import com.payu.myshop.transactionalms.domain.models.dto.ShippingAddress;
import com.payu.myshop.transactionalms.domain.models.dto.Transaccion;
import com.payu.myshop.transactionalms.domain.models.dto.TransactionResponse;
import com.payu.myshop.transactionalms.domain.models.endpoints.AuthorizePaymentRequest;
import com.payu.myshop.transactionalms.domain.models.endpoints.RefundRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;


public class SampleDto {

    public static AuthorizePaymentRequest getAuthorizePaymentRequest(Boolean token){
        AuthorizePaymentRequest request = AuthorizePaymentRequest.builder()
                .totalTax(0L)
                .totalPrice(0L)
                .buyer(getBuyerDto())
                .creditCard(getCreditCardDto())
                .currency("testCurrency")
                .payer(getPayerDto())
                .paymentCountry("testCountry")
                .paymentMethod("testMethod")
                .build();

        if (token){
            request.setCreditCardTokenId("testTokenId");
        }
        return request;
    }

    public static RefundRequest getRefundRequest(){
        return RefundRequest.builder()
                    .observacion("testObservacion")
                    .pagoId(0L)
                .build();
    }

    public static  Buyer getBuyerDto(){
        return Buyer.builder()
                    .shippingAddress(getAdressDto())
                    .dniNumber("testDni")
                    .contactPhone("testPhone")
                    .merchantBuyerId("testIdt")
                    .emailAddress("testName")
                    .fullName("testName")
                .build();
    }

    public static  Payer getPayerDto(){
        return Payer.builder()
                .billingAddress(BillingAddress.builder().build())
                .dniNumber("testDni")
                .contactPhone("testPhone")
                .merchantPayerId("testIdt")
                .emailAddress("testName")
                .fullName("testName")
                .build();
    }

    public static  CreditCard getCreditCardDto(){
        return CreditCard.builder()
                .securityCode("testNumber")
                .expirationDate("testMonth/yearMonth")
                .name("nameTest")
                .number("testNumber")
            .build();
    }

    public static  ShippingAddress getAdressDto(){
        return ShippingAddress.builder()
                    .city("testCity")
                    .country("testCountry")
                    .phone("testPhone")
                    .state("testState")
                    .postalCode("testPostalCode")
                    .street1("testStreet1")
                    .street2("testStreet2")
                .build();
    }

    public static  ResponsePaymentPayu getResponsePaymentPayu(){
        ResponsePaymentPayu request = ResponsePaymentPayu.builder()
                    .error("testError")
                    .code("testCode")
                    .transactionResponse(getTransactionResponse())
                .build();

        return request;
    }
    public static ResponseCreateTokenPayu getResponseCreateTokenPayu(){
        return ResponseCreateTokenPayu.builder()
                    .error("testError")
                    .code("testCode")
                    .creditCardToken(getCreditCardToken())
                .build();
    }

    public static CreditCardToken getCreditCardToken(){
        return CreditCardToken.builder()
                    .expirationDate("test_expirationDate")
                    .number("test_number")
                    .paymentMethod("test_paymentMethod")
                    .identificationNumber("test_identificationNumber")
                    .name("test_name")
                    .payerId("test_payerId")
                    .creditCardTokenId("test_creditCardTokenId")
                    .errorDescription("test_creditCardTokenId")
                    .maskedNumber("test_creditCardTokenId")
                .build();
    }



    public static  TransactionResponse getTransactionResponse(){
        return TransactionResponse.builder()
                .authorizationCode("testAuthCode")
                .errorCode("testErrorCode")
                .transactionId("testTransactionId")
                .orderId(0L)
                .paymentNetworkResponseCode("testPaymentNet")
                .paymentNetworkResponseErrorMessage("testPaymentNet")
                .state("testState")
                .additionalInfo(AdditionalInfo.builder()
                        .cardType("testCard")
                        .transactionType("testTransaType")
                        .paymentNetwork("testPaymentNet")
                        .rejectionType("testRejection")
                        .responseNetworkMessage("testResponseNet")
                        .travelAgencyAuthorizationCode("testTravelAgency")
                        .build())
                .extraParameters(ExtraParametersResp.builder()
                        .BANK_REFERENCED_CODE("testBankRef")
                        .build())
                .build();

    }

    public static Pago getPagoFactory(){
        return Pago.builder()
                .idPago(1L)
                .payuOrdenId(1L)
                .build();
    }

    public static Transaccion getTransaccionFactory() {
        return Transaccion.builder()
                .idTransaccion(1L)
                .payuTransaccionId("testPayuTransaccionId")
                .build();
    }

    public static List<Transaccion> getTransaccionList() {
        return Arrays.asList(Transaccion.builder()
                    .idTransaccion(1L)
                    .transactionType("AUTHORIZATION_AND_CAPTURE")
                .build());

    }
}
