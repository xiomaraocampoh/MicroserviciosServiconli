package com.serviconli.task.dto;

import com.serviconli.task.model.EstadoTarea;
import com.serviconli.task.model.Prioridad;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TareaResponseDTO {
    private Long id;
    private String tipo;
    private String paciente;
    private String eps;
    private Prioridad prioridad;
    private EstadoTarea estado;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private LocalDateTime fechaRecordatorio;
    private String telefono;
    private String doctor;
    private String ubicacion;
    private String fecha;
    private String hora;

    // Podríamos incluir una lista de HistorialTareaResponseDTO aquí si queremos el historial directamente en la respuesta de la tarea
}