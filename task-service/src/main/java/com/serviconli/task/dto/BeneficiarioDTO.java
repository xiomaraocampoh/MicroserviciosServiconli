package com.serviconli.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiarioDTO {
    private String nombreCompleto;
    private String tipoIdentificacion;
    private String numeroIdentificacion;
    private LocalDate fechaExpedicion;
    private String celular;
    private String parentesco;
    private String eps;

    // Datos del cotizante asociado
    private String nombreCotizante;
    private String tipoIdentificacionCotizante;
    private String numeroIdentificacionCotizante;
}
