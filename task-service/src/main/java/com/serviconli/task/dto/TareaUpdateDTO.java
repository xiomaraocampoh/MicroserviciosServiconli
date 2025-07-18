package com.serviconli.task.dto;


import com.serviconli.task.model.Prioridad;
import com.serviconli.task.model.EstadoTarea;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private LocalDateTime fechaRecordatorio;
    private String telefono;
    private String doctor;
    private String ubicacion;
    private String fecha;
    private String hora;

}