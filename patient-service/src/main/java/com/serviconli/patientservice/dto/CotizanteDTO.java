package com.serviconli.patientservice.dto;

import lombok.Data;

@Data
public class CotizanteDTO {
    private Long id;
    private String nombreCompleto;
    private String tipoIdentificacion;
    private String numeroIdentificacion;
    private String celular;
    private String correo;


}
