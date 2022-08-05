package com.payu.myshop.ventasms.domain.models.endpoints;

import com.payu.myshop.ventasms.domain.models.dto.TipoRespuesta;
import lombok.Data;

import java.util.List;

@Data
public class ResponseWsVentas {

    private TipoRespuesta tipoRespuesta;	//Enumerador: Puede ser Exito o error.
    private String mensaje = "";
    private long totalItems = 0;
    private Object resultado;
    private List<Object> listaResultado;

}
