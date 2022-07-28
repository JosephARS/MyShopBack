package com.payu.myshop.transactionalms.domain.ports.services;

import com.payu.myshop.transactionalms.domain.models.dto.Pago;
import com.payu.myshop.transactionalms.domain.models.dto.ResponsePaymentPayu;
import com.payu.myshop.transactionalms.domain.models.dto.Transaccion;
import com.payu.myshop.transactionalms.domain.models.endpoints.AuthorizePaymentRequest;
import com.payu.myshop.transactionalms.domain.models.endpoints.RefundRequest;
import com.payu.myshop.transactionalms.domain.models.endpoints.ResponseWS;

import java.security.NoSuchAlgorithmException;

public interface TransactionalService {

    ResponseWS authorizePayment(AuthorizePaymentRequest request);

    ResponseWS refundPayment(RefundRequest request);

    ResponseWS authorizePaymentWithToken(AuthorizePaymentRequest request);

}
