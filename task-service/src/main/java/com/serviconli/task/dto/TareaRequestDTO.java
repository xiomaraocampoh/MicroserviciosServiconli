package com.serviconli.task.dto;


import com.serviconli.task.model.Prioridad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TareaRequestDTO {

    @NotBlank(message = "El tipo de tarea no puede estar vacío")
    @Size(max = 50, message = "El tipo de tarea no puede exceder 50 caracteres")
    private String tipo; // Ejemplo: "cita", "llamada", "reprogramación"

    @NotBlank(message = "El nombre del paciente no puede estar vacío")
    @Size(max = 255, message = "El nombre del paciente no puede exceder 255 caracteres")
    private String paciente;

    @NotBlank(message = "La EPS no puede estar vacía")
    @Size(max = 100, message = "La EPS no puede exceder 100 caracteres")
    private String eps;

    @NotNull(message = "La prioridad no puede ser nula")
    private Prioridad prioridad;

    @Size(max = 1000, message = "Las observaciones no pueden exceder 1000 caracteres")
    private String observaciones;

    private LocalDateTime fechaRecordatorio;

    private String telefono;
    private String doctor;
    private String ubicacion;
    private String fecha;
    private String hora;


}