package com.serviconli.task.dto;


import com.serviconli.task.model.Prioridad;
import com.serviconli.task.model.EstadoTarea;
import com.serviconli.task.model.TipoPaciente;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TareaUpdateDTO {
    @Size(max = 50, message = "El tipo de tarea no puede exceder 50 caracteres")
    private String tipo;

    @Size(max = 255, message = "El nombre del paciente no puede exceder 255 caracteres")
    private String paciente;

    @Size(max = 100, message = "La EPS no puede exceder 100 caracteres")
    private String eps;

    private Prioridad prioridad;

    private EstadoTarea estado; // Permitimos actualizar el estado directamente

    @Size(max = 1000, message = "Las observaciones no pueden exceder 1000 caracteres")
    private String observaciones;

    private TipoPaciente tipoPaciente; // COTIZANTE o BENEFICIARIO

    @Size(max = 50, message = "El tipo de paciente no puede exceder 50 caracteres")
    private String tipoIdentificacionPaciente;

    @Size(max = 250, message = "El numero de identificación puede exceder 250 caracteres")
    private String numeroIdentificacionPaciente;

    private LocalDate fechaExpedicion;

    @Size(max = 20, message = "El numero de celular no puede exceder 20 caracteres")
    private String celularPaciente;

    @Size(max = 100, message = "El parentezco no puede exceder 100 caracteres")
    private String parentezco; // solo si es beneficiario

    @Size(max = 250, message = "El nombre del cotizante no puede exceder 250 caracteres")
    private String nombreCotizante; // solo si es beneficiario

    @Size(max = 250, message = "El numero de identificación no puede exceder 250 caracteres")
    private String numeroIdentificacionCotizante;

    @Size(max = 100, message = "El numero de autorización no puede exceder 250 caracteres")
    private String numeroAutorizacion;

    @Size(max = 100, message = "El numero de radicado no puede exceder 100 caracteres")
    private String numeroRadicado;

    @Size(max = 500, message = "las especificaciones no pueden exceder 500 caracteres")
    private String especificaciones;

    private LocalDateTime fechaRecordatorio;
    private String telefono;
    private String doctor;
    private String ubicacion;
    private String fecha;
    private String hora;

}