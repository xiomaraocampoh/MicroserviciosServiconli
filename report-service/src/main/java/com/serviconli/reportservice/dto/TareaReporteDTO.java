package com.serviconli.reportservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO para recibir los datos de las tareas que se incluirán en el reporte.
 * Contiene solo los campos necesarios para el archivo Excel.
 */
@Data
public class TareaReporteDTO {
    private Long id;
    private String tipo;
    private String paciente;
    private String eps;
    private String prioridad;
    private String estado;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaRecordatorio;
    private String asignadoA; // Campo para el email o nombre del responsable
}
