package com.payu.myshop.inventarioms.domain.models.endpoints;

import lombok.Data;

import java.util.List;

@Data
public class ResponseWS {

    private TipoRespuesta tipoRespuesta;	//Enumerador: Puede ser Exito o error.
    private String mensaje = "";
    private long totalItems = 0;
    private Object resultado;
    private List<Object> listaResultado;

}
