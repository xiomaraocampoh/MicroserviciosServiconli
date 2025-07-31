package com.serviconli.patientservice.dto;

import lombok.Data;

@Data
public class BusquedaPacienteResponseDTO {

    private String tipoPaciente; // "COTIZANTE" o "BENEFICIARIO"
    private Long id;
    private String nombreCompleto;
    private String tipoIdentificacion;
    private String numeroIdentificacion;
    private String celular;
    private String parentezco;
    private String nombreCotizante;
    private String numeroIdentificacionCotizante;

}
