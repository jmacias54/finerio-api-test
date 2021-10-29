package com.finerio.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "ResponseError", description = "Respuesta de error a un fallo en el sistema")
@JsonPropertyOrder({ "ResponseError" })
public class ResponseError {
    
	@JsonProperty("codigo")
    @ApiModelProperty(value = "errorCode - codigo asignado a un error", required = true)
    private int errorCode;
    
	@JsonProperty("clave")
    @ApiModelProperty(value = "httpStatus", required = true)
    private String httpStatus;
    
	
	@JsonProperty("errorMessage")
    @ApiModelProperty(value = "errorMessage - error descrito de manera clara", required = true)
    private String errorMessage;
    
    
	@JsonProperty("descripcion")
    @ApiModelProperty(value = "rootErrorMessage error general", required = true)
    private String rootErrorMessage;
    
	@JsonProperty("mensajes")
    @ApiModelProperty(value = "errorList lista de errores", required = true)
    private List<String> errorList;
}
