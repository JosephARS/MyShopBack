package com.payu.myshop.transactionalms.domain.usecases;

import com.payu.myshop.transactionalms.domain.models.dto.Pago;
import com.payu.myshop.transactionalms.domain.models.dto.ResponseCreateTokenPayu;
import com.payu.myshop.transactionalms.domain.models.dto.ResponsePaymentPayu;
import com.payu.myshop.transactionalms.domain.models.dto.TipoRespuesta;
import com.payu.myshop.transactionalms.domain.models.dto.Transaccion;
import com.payu.myshop.transactionalms.domain.models.endpoints.AuthorizePaymentRequest;
import com.payu.myshop.transactionalms.domain.models.endpoints.RefundRequest;
import com.payu.myshop.transactionalms.domain.models.endpoints.ResponseWsTransactional;
import com.payu.myshop.transactionalms.domain.ports.repositories.PagosRepositoryPort;
import com.payu.myshop.transactionalms.domain.ports.repositories.TransaccionesRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static com.payu.myshop.transactionalms.utils.dto.SampleDto.getAuthorizePaymentRequest;
import static com.payu.myshop.transactionalms.utils.dto.SampleDto.getPagoFactory;
import static com.payu.myshop.transactionalms.utils.dto.SampleDto.getRefundRequest;
import static com.payu.myshop.transactionalms.utils.dto.SampleDto.getResponseCreateTokenPayu;
import static com.payu.myshop.transactionalms.utils.dto.SampleDto.getResponsePaymentPayu;
import static com.payu.myshop.transactionalms.utils.dto.SampleDto.getTransaccionFactory;
import static com.payu.myshop.transactionalms.utils.dto.SampleDto.getTransaccionList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class TransactionalServiceImplTest {

    @Mock
    RestTemplate restTemplate;

    @Mock
    PagosRepositoryPort pagosRepositoryMock;

    @Mock
    SampleFactory sampleFactory;

    @Mock
    ResponsePaymentPayu responseMock;

    @Mock
    AuthorizePaymentRequest requestPaymentMock;

    @Mock
    TransaccionesRepositoryPort transaccionesRepositoryMock;

    @InjectMocks
    TransactionalServiceImpl transactionalService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(transactionalService, "URL_STRING", "https://testUrl.com");
    }

    @MockitoSettings(strictness = Strictness.WARN)
    @Test
    void authorizePayment() {

        requestPaymentMock = getAuthorizePaymentRequest(false);
        responseMock = getResponsePaymentPayu();

        Mockito.when(restTemplate.postForEntity(
                Mockito.anyString(),
                Mockito.any(HttpEntity.class),
                Mockito.eq(ResponsePaymentPayu.class))
        ).thenReturn(new ResponseEntity<>(responseMock, HttpStatus.OK));

        Mockito.when(pagosRepositoryMock.savePago(any(Pago.class))).thenReturn(getPagoFactory());
        Mockito.when(transaccionesRepositoryMock.saveTransaccion(any(Transaccion.class))).thenReturn(getTransaccionFactory());

        ResponseWsTransactional oResponseWsTransactional = transactionalService.authorizePayment(requestPaymentMock);
        assertEquals(TipoRespuesta.Exito, oResponseWsTransactional.getTipoRespuesta());

    }

    @MockitoSettings(strictness = Strictness.WARN)
    @Test
    void refundPayment() {
        RefundRequest requestRefundMock = getRefundRequest();
        responseMock = getResponsePaymentPayu();

        Mockito.when(restTemplate.postForEntity(
                Mockito.anyString(),
                Mockito.any(HttpEntity.class),
                Mockito.eq(ResponsePaymentPayu.class))
        ).thenReturn(new ResponseEntity<>(responseMock, HttpStatus.OK));

        Mockito.when(pagosRepositoryMock.findById(anyLong())).thenReturn(getPagoFactory());
        Mockito.when(transaccionesRepositoryMock.findByPago(anyLong())).thenReturn(getTransaccionList());

        ResponseWsTransactional oResponseWsTransactional = transactionalService.refundPayment(requestRefundMock);
        assertEquals(TipoRespuesta.Exito, oResponseWsTransactional.getTipoRespuesta());
    }

    @MockitoSettings(strictness = Strictness.WARN)
    @Test
    void authorizePaymentWithToken_savedCreditCard() {

        AuthorizePaymentRequest requestMock = getAuthorizePaymentRequest(true);
        ResponsePaymentPayu responsePaymentMock = getResponsePaymentPayu();

        Mockito.when(restTemplate.postForEntity(
                Mockito.anyString(),
                Mockito.any(HttpEntity.class),
                Mockito.eq(ResponsePaymentPayu.class))
        ).thenReturn(new ResponseEntity<>(responsePaymentMock, HttpStatus.OK));

        Mockito.when(pagosRepositoryMock.savePago(any(Pago.class))).thenReturn(getPagoFactory());
        Mockito.when(transaccionesRepositoryMock.saveTransaccion(any(Transaccion.class))).thenReturn(getTransaccionFactory());

        ResponseWsTransactional oResponseWsTransactional = transactionalService.authorizePaymentWithToken(requestMock);
        assertEquals(TipoRespuesta.Exito, oResponseWsTransactional.getTipoRespuesta());

    }

    @MockitoSettings(strictness = Strictness.WARN)
    @Test
    void authorizePaymentWithToken_nonSavedCard() {

        AuthorizePaymentRequest requestMock = getAuthorizePaymentRequest(false);
        ResponseCreateTokenPayu responseCreateTokenMock = getResponseCreateTokenPayu();
        ResponsePaymentPayu responsePaymentMock = getResponsePaymentPayu();

        Mockito.when(restTemplate.postForEntity(
                Mockito.anyString(),
                Mockito.any(HttpEntity.class),
                Mockito.eq(ResponseCreateTokenPayu.class))
        ).thenReturn(new ResponseEntity<>(responseCreateTokenMock, HttpStatus.OK));

        Mockito.when(restTemplate.postForEntity(
                Mockito.anyString(),
                Mockito.any(HttpEntity.class),
                Mockito.eq(ResponsePaymentPayu.class))
        ).thenReturn(new ResponseEntity<>(responsePaymentMock, HttpStatus.OK));

        Mockito.when(pagosRepositoryMock.savePago(any(Pago.class))).thenReturn(getPagoFactory());
        Mockito.when(transaccionesRepositoryMock.saveTransaccion(any(Transaccion.class))).thenReturn(getTransaccionFactory());

        ResponseWsTransactional oResponseWsTransactional = transactionalService.authorizePaymentWithToken(requestMock);
        assertEquals(TipoRespuesta.Exito, oResponseWsTransactional.getTipoRespuesta());

    }
}