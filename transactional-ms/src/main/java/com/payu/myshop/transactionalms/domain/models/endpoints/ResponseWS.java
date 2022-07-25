package com.payu.myshop.transactionalms.domain.models.endpoints;

import com.payu.myshop.transactionalms.domain.models.dto.TipoRespuesta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWS {

    private TipoRespuesta tipoRespuesta;	//Enumerador: Puede ser Exito o error.
    private String mensaje = "";
    private long totalItems = 0;
    private Object resultado;
    private List<Object> listaResultado;
}
