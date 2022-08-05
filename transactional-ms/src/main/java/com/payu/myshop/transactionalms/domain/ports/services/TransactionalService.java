package com.payu.myshop.transactionalms.domain.ports.services;

import com.payu.myshop.transactionalms.domain.models.endpoints.AuthorizePaymentRequest;
import com.payu.myshop.transactionalms.domain.models.endpoints.RefundRequest;
import com.payu.myshop.transactionalms.domain.models.endpoints.ResponseWsTransactional;

public interface TransactionalService {

    ResponseWsTransactional authorizePayment(AuthorizePaymentRequest request);

    ResponseWsTransactional refundPayment(RefundRequest request);

    ResponseWsTransactional authorizePaymentWithToken(AuthorizePaymentRequest request);

}
