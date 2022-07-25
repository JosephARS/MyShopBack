package com.payu.myshop.transactionalms.domain.ports.services;

import com.payu.myshop.transactionalms.domain.models.endpoints.AuthorizePaymentRequest;
import com.payu.myshop.transactionalms.domain.models.endpoints.RefundRequest;
import com.payu.myshop.transactionalms.domain.models.endpoints.ResponseWS;

import java.security.NoSuchAlgorithmException;

public interface TransactionalService {

    ResponseWS authorizePayment(AuthorizePaymentRequest request);

    ResponseWS refundPayment(RefundRequest request);

    String createSignature(String parametros) ;

    ResponseWS getPaymentList();

}
